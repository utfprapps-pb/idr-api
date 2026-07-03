package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncCommand;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncUseCase;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.property_management.sync.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.SyncEntityStatus;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Map.entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UploadSyncUseCase")
class UploadSyncUseCaseTest {

    private static final String VALID_CPF = "529.982.247-25";

    @Mock ProducerGateway producerGateway;
    @Mock PropertyGateway propertyGateway;
    @InjectMocks UploadSyncUseCase useCase;

    @Test
    @DisplayName("deve criar produtor novo e propriedade vinculada")
    void shouldCreateNewProducerAndProperty() {
        final var producerLocalId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var savedProducer = Producer.with(ProducerID.unique(), "João Silva", CPF.from(VALID_CPF));
        final var savedProperty = stubSavedProperty(savedProducer.getId().id(), cityId);

        when(producerGateway.existsByCpf(any())).thenReturn(false);
        when(producerGateway.save(any())).thenReturn(savedProducer);
        when(propertyGateway.save(any())).thenReturn(savedProperty);

        final var command = new UploadSyncCommand(List.of(
                producerEntity(producerLocalId, "João Silva", VALID_CPF),
                propertyEntity(UUID.randomUUID(), producerLocalId, cityId)
        ));

        final var results = useCase.execute(command);

        assertEquals(2, results.size());
        assertEquals(SyncEntityStatus.CREATED, results.get(0).status());
        assertEquals(SyncEntityStatus.CREATED, results.get(1).status());
    }

    @Test
    @DisplayName("deve retornar EXISTING para produtor com CPF duplicado")
    void shouldReturnExistingForDuplicateCpf() {
        final var producerLocalId = UUID.randomUUID();
        final var existing = Producer.with(ProducerID.unique(), "José", CPF.from(VALID_CPF));

        when(producerGateway.existsByCpf(any())).thenReturn(true);
        when(producerGateway.findByCpf(any())).thenReturn(Optional.of(existing));

        final var command = new UploadSyncCommand(List.of(
                producerEntity(producerLocalId, "José", VALID_CPF)
        ));

        final var results = useCase.execute(command);

        assertEquals(1, results.size());
        assertEquals(SyncEntityStatus.EXISTING, results.getFirst().status());
        assertEquals(existing.getId().id(), results.getFirst().serverId());
    }

    @Test
    @DisplayName("deve lançar exceção quando batch excede 100 entidades")
    void shouldThrowWhenBatchExceeds100() {
        final var entities = new ArrayList<UploadSyncCommand.OfflineEntityCommand>();
        for (int i = 0; i <= 100; i++) {
            entities.add(producerEntity(UUID.randomUUID(), "Nome " + i, "000.000.000-0" + (i % 10)));
        }
        final var command = new UploadSyncCommand(entities);

        assertThrows(DomainException.class, () -> useCase.execute(command));
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException quando PROPERTY tem producerLocalId inválido e sem producerId")
    void shouldThrowWhenPropertyHasInvalidProducerLocalId() {
        final var command = new UploadSyncCommand(List.of(
                propertyEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        ));

        assertThrows(UnprocessableEntityException.class, () -> useCase.execute(command));
    }

    @Test
    @DisplayName("deve vincular propriedade a produtor existente selecionado via producerId")
    void shouldLinkPropertyToExistingProducerViaProducerId() {
        final var existingProducerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var savedProperty = stubSavedProperty(existingProducerId, cityId);

        when(producerGateway.existsById(ProducerID.from(existingProducerId))).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(savedProperty);

        final var command = new UploadSyncCommand(List.of(
                propertyEntityWithDirectProducerId(UUID.randomUUID(), existingProducerId, cityId)
        ));

        final var results = useCase.execute(command);

        assertEquals(1, results.size());
        assertEquals(SyncEntityStatus.CREATED, results.getFirst().status());
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException quando producerId direto não existe no servidor")
    void shouldThrowWhenDirectProducerIdNotFound() {
        final var nonExistentProducerId = UUID.randomUUID();

        when(producerGateway.existsById(ProducerID.from(nonExistentProducerId))).thenReturn(false);

        final var command = new UploadSyncCommand(List.of(
                propertyEntityWithDirectProducerId(UUID.randomUUID(), nonExistentProducerId, UUID.randomUUID())
        ));

        assertThrows(UnprocessableEntityException.class, () -> useCase.execute(command));
    }

    @Test
    @DisplayName("deve usar zero quando campos numéricos estão ausentes no payload")
    void shouldDefaultToZeroForMissingNumericFields() {
        final var existingProducerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var savedProperty = stubSavedProperty(existingProducerId, cityId);

        when(producerGateway.existsById(ProducerID.from(existingProducerId))).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(savedProperty);

        final var command = new UploadSyncCommand(List.of(
                propertyEntityWithoutNumericFields(UUID.randomUUID(), existingProducerId, cityId)
        ));

        final var results = useCase.execute(command);

        assertEquals(1, results.size());
        assertEquals(SyncEntityStatus.CREATED, results.getFirst().status());
    }

    @Test
    @DisplayName("deve aceitar UUID, BigDecimal, Double e Number tipados diretamente no payload")
    void shouldAcceptTypedUuidAndNumericValues() {
        final var existingProducerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var savedProperty = stubSavedProperty(existingProducerId, cityId);

        when(producerGateway.existsById(ProducerID.from(existingProducerId))).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(savedProperty);

        final var command = new UploadSyncCommand(List.of(
                propertyEntityWithTypedValues(UUID.randomUUID(), existingProducerId, cityId)
        ));

        final var results = useCase.execute(command);

        assertEquals(1, results.size());
        assertEquals(SyncEntityStatus.CREATED, results.getFirst().status());
    }

    @Test
    @DisplayName("deve processar PRODUCER antes de PROPERTY independente da ordem no payload")
    void shouldProcessProducerBeforeProperty() {
        final var producerLocalId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var savedProducer = Producer.with(ProducerID.unique(), "Ana", CPF.from(VALID_CPF));
        final var savedProperty = stubSavedProperty(savedProducer.getId().id(), cityId);

        when(producerGateway.existsByCpf(any())).thenReturn(false);
        when(producerGateway.save(any())).thenReturn(savedProducer);
        when(propertyGateway.save(any())).thenReturn(savedProperty);

        final var command = new UploadSyncCommand(List.of(
                propertyEntity(UUID.randomUUID(), producerLocalId, cityId),
                producerEntity(producerLocalId, "Ana", VALID_CPF)
        ));

        final var results = useCase.execute(command);

        assertEquals(2, results.size());
        final var producerResult = results.stream()
                .filter(r -> r.status() == SyncEntityStatus.CREATED && r.serverId().equals(savedProducer.getId().id()))
                .findFirst();
        assertTrue(producerResult.isPresent());
    }

    private UploadSyncCommand.OfflineEntityCommand producerEntity(
            final UUID localId, final String name, final String cpf) {
        return new UploadSyncCommand.OfflineEntityCommand(
                OfflineEntityType.PRODUCER, localId, Map.of("name", name, "cpf", cpf));
    }

    private UploadSyncCommand.OfflineEntityCommand propertyEntity(
            final UUID localId, final UUID producerLocalId, final UUID cityId) {
        return new UploadSyncCommand.OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerLocalId", producerLocalId.toString()),
                        entry("cityId", cityId.toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0"),
                        entry("nakedAveragePrice", "0.0"),
                        entry("leaseAveragePrice", "0.0"),
                        entry("dairyCattleFarming", "0.0"),
                        entry("perennialPasture", "0.0"),
                        entry("summerPlowing", "0.0"),
                        entry("winterPlowing", "0.0")));
    }

    private UploadSyncCommand.OfflineEntityCommand propertyEntityWithDirectProducerId(
            final UUID localId, final UUID producerId, final UUID cityId) {
        return new UploadSyncCommand.OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerId", producerId.toString()),
                        entry("cityId", cityId.toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0"),
                        entry("nakedAveragePrice", "0.0"),
                        entry("leaseAveragePrice", "0.0"),
                        entry("dairyCattleFarming", "0.0"),
                        entry("perennialPasture", "0.0"),
                        entry("summerPlowing", "0.0"),
                        entry("winterPlowing", "0.0")));
    }

    private UploadSyncCommand.OfflineEntityCommand propertyEntityWithoutNumericFields(
            final UUID localId, final UUID producerId, final UUID cityId) {
        return new UploadSyncCommand.OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerId", producerId.toString()),
                        entry("cityId", cityId.toString())));
    }

    private UploadSyncCommand.OfflineEntityCommand propertyEntityWithTypedValues(
            final UUID localId, final UUID producerId, final UUID cityId) {
        return new UploadSyncCommand.OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerId", producerId),
                        entry("cityId", cityId),
                        entry("latitude", new BigDecimal("-25.43")),
                        entry("longitude", -49),
                        entry("nakedAveragePrice", new BigDecimal("100.50")),
                        entry("leaseAveragePrice", 50),
                        entry("dairyCattleFarming", 1.5d),
                        entry("perennialPasture", 2),
                        entry("summerPlowing", 3.0d),
                        entry("winterPlowing", 4)));
    }

    private Property stubSavedProperty(final UUID producerId, final UUID cityId) {
        return Property.with(
                PropertyID.unique(), "Fazenda Teste",
                Coord.from(BigDecimal.ZERO, BigDecimal.ZERO),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                br.gov.pr.idr.domain.property_management.producer.ProducerID.from(producerId),
                br.gov.pr.idr.domain.property_management.city.CityID.from(cityId),
                List.of(), List.of()
        );
    }
}
