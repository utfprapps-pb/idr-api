package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.application.property_management.sync.upload.OfflineEntityCommand;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncCommand;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncUseCase;
import br.gov.pr.idr.infra.property_management.producer.handler.ProducerSyncHandler;
import br.gov.pr.idr.infra.property_management.property.sync.PropertySyncHandler;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMapping;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Upload sync (integração do pipeline)")
class UploadSyncIntegrationTest {

    private static final String CPF_A = "529.982.247-25";
    private static final String CPF_B = "111.444.777-35";

    private FakeProducerGateway producerGateway;
    private FakePropertyGateway propertyGateway;
    private FakeSyncIdMappingGateway idMappingGateway;
    private UploadSyncUseCase useCase;

    @BeforeEach
    void setUp() {
        producerGateway = new FakeProducerGateway();
        propertyGateway = new FakePropertyGateway();
        idMappingGateway = new FakeSyncIdMappingGateway();
        useCase = new UploadSyncUseCase(
                List.of(
                        new PropertySyncHandler(propertyGateway, producerGateway, new FakeStorageGateway()),
                        new ProducerSyncHandler(producerGateway)
                ),
                idMappingGateway);
    }

    @Test
    @DisplayName("deve criar produtor e propriedade vinculada num único batch")
    void shouldCreateProducerAndLinkedProperty() {
        final var technicianId = UserID.unique();
        final var producerLocalId = UUID.randomUUID();
        final var propertyLocalId = UUID.randomUUID();

        final var results = useCase.execute(new UploadSyncCommand(technicianId, List.of(
                property(propertyLocalId, producerLocalId),
                producer(producerLocalId, CPF_A))));

        assertEquals(SyncEntityStatus.CREATED, statusOf(results, producerLocalId));
        assertEquals(SyncEntityStatus.CREATED, statusOf(results, propertyLocalId));
        assertEquals(1, producerGateway.saveCount);
        assertEquals(1, propertyGateway.saveCount);
        // a propriedade foi vinculada ao serverId do produtor recém-criado
        final var producerServerId = serverIdOf(results, producerLocalId);
        assertEquals(producerServerId, propertyGateway.last.getProducerId().id());
    }

    @Test
    @DisplayName("deve ser idempotente ao reenviar o mesmo batch (retry de rede)")
    void shouldBeIdempotentOnRetry() {
        final var technicianId = UserID.unique();
        final var producerLocalId = UUID.randomUUID();
        final var propertyLocalId = UUID.randomUUID();
        final var batch = List.of(
                producer(producerLocalId, CPF_A),
                property(propertyLocalId, producerLocalId));

        final var first = useCase.execute(new UploadSyncCommand(technicianId, batch));
        final var second = useCase.execute(new UploadSyncCommand(technicianId, batch));

        assertEquals(SyncEntityStatus.EXISTING, statusOf(second, producerLocalId));
        assertEquals(SyncEntityStatus.EXISTING, statusOf(second, propertyLocalId));
        // mesmos serverIds, sem duplicação
        assertEquals(serverIdOf(first, producerLocalId), serverIdOf(second, producerLocalId));
        assertEquals(serverIdOf(first, propertyLocalId), serverIdOf(second, propertyLocalId));
        assertEquals(1, producerGateway.saveCount);
        assertEquals(1, propertyGateway.saveCount);
    }

    @Test
    @DisplayName("deve reprocessar apenas o que faltou quando o batch anterior falhou parcialmente")
    void shouldReprocessOnlyMissingAfterPartialFailure() {
        final var technicianId = UserID.unique();
        final var producerLocalId = UUID.randomUUID();
        final var propertyLocalId = UUID.randomUUID();

        // batch 1: produtor cria, propriedade falha (referência inexistente)
        final var first = useCase.execute(new UploadSyncCommand(technicianId, List.of(
                producer(producerLocalId, CPF_A),
                property(propertyLocalId, UUID.randomUUID()))));

        assertEquals(SyncEntityStatus.CREATED, statusOf(first, producerLocalId));
        assertEquals(SyncEntityStatus.FAILED, statusOf(first, propertyLocalId));

        // batch 2 (reenvio completo, agora com referência válida ao produtor do batch)
        final var second = useCase.execute(new UploadSyncCommand(technicianId, List.of(
                producer(producerLocalId, CPF_A),
                property(propertyLocalId, producerLocalId))));

        assertEquals(SyncEntityStatus.EXISTING, statusOf(second, producerLocalId));
        assertEquals(SyncEntityStatus.CREATED, statusOf(second, propertyLocalId));
        assertEquals(1, producerGateway.saveCount);
        assertEquals(1, propertyGateway.saveCount);
    }

    @Test
    @DisplayName("deve tratar o mesmo localId de técnicos distintos como registros independentes")
    void shouldIsolateSameLocalIdAcrossTechnicians() {
        final var localId = UUID.randomUUID();

        final var techA = useCase.execute(new UploadSyncCommand(UserID.unique(), List.of(
                producer(localId, CPF_A))));
        final var techB = useCase.execute(new UploadSyncCommand(UserID.unique(), List.of(
                producer(localId, CPF_B))));

        assertEquals(SyncEntityStatus.CREATED, statusOf(techA, localId));
        assertEquals(SyncEntityStatus.CREATED, statusOf(techB, localId));
        assertNotEquals(serverIdOf(techA, localId), serverIdOf(techB, localId));
        assertEquals(2, producerGateway.saveCount);
    }

    // --- helpers ---

    private static SyncEntityStatus statusOf(final List<SyncEntityResult> results, final UUID localId) {
        return result(results, localId).status();
    }

    private static UUID serverIdOf(final List<SyncEntityResult> results, final UUID localId) {
        return result(results, localId).serverId();
    }

    private static SyncEntityResult result(final List<SyncEntityResult> results, final UUID localId) {
        return results.stream().filter(r -> r.localId().equals(localId)).findFirst().orElseThrow();
    }

    private static OfflineEntityCommand producer(final UUID localId, final String cpf) {
        return new OfflineEntityCommand(
                OfflineEntityType.PRODUCER, localId, Map.of("name", "Produtor", "cpf", cpf));
    }

    private static OfflineEntityCommand property(final UUID localId, final UUID producerLocalId) {
        return new OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda"),
                        entry("producerLocalId", producerLocalId.toString()),
                        entry("cityId", UUID.randomUUID().toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0")));
    }

    // --- in-memory fakes ---

    private static final class FakeProducerGateway implements ProducerGateway {
        private final Map<UUID, Producer> byId = new HashMap<>();
        int saveCount = 0;

        @Override public Producer save(final Producer producer) {
            byId.put(producer.getId().id(), producer);
            saveCount++;
            return producer;
        }
        @Override public Producer update(final Producer producer) { return save(producer); }
        @Override public Optional<Producer> findById(final ProducerID id) { return Optional.ofNullable(byId.get(id.id())); }
        @Override public boolean existsById(final ProducerID id) { return byId.containsKey(id.id()); }
        @Override public boolean existsByCpf(final CPF cpf) { return findByCpf(cpf).isPresent(); }
        @Override public Optional<Producer> findByCpf(final CPF cpf) {
            return byId.values().stream().filter(p -> p.getCpf().value().equals(cpf.value())).findFirst();
        }
        @Override public Pagination<Producer> search(final SearchQuery query) { throw new UnsupportedOperationException(); }
    }

    private static final class FakePropertyGateway implements PropertyGateway {
        int saveCount = 0;
        Property last;

        @Override public Property save(final Property property) {
            saveCount++;
            last = property;
            return property;
        }
        @Override public Property update(final Property property) { return save(property); }
        @Override public Optional<Property> findById(final PropertyID id) { return Optional.empty(); }
        @Override public Optional<GetPropertyQueryResult> findByIdWithDetails(final PropertyID id) { return Optional.empty(); }
        @Override public Pagination<Property> search(final SearchQuery query,
                final br.gov.pr.idr.domain.property_management.property.PropertySearchScope scope) {
            throw new UnsupportedOperationException();
        }
        @Override public boolean existsById(final PropertyID id) { return false; }
        @Override public void deleteById(final PropertyID id) { }
    }

    private static final class FakeStorageGateway implements StorageGateway {
        @Override public void store(final String key, final String contentType, final byte[] content) { }
        @Override public byte[] retrieve(final String key) { return new byte[0]; }
        @Override public void delete(final String key) { }
    }

    private static final class FakeSyncIdMappingGateway implements SyncIdMappingGateway {
        private final Map<String, SyncIdMapping> store = new HashMap<>();

        private String key(final UserID technicianId, final UUID localId) {
            return technicianId.id() + ":" + localId;
        }
        @Override public boolean existsByTechnicianAndLocalId(final UserID technicianId, final UUID localId) {
            return store.containsKey(key(technicianId, localId));
        }
        @Override public Optional<UUID> findServerId(final UserID technicianId, final UUID localId) {
            return Optional.ofNullable(store.get(key(technicianId, localId))).map(SyncIdMapping::serverId);
        }
        @Override public SyncIdMapping save(final SyncIdMapping mapping) {
            store.put(key(mapping.technicianId(), mapping.localId()), mapping);
            return mapping;
        }
    }
}
