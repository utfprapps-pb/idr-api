package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.application.property_management.sync.upload.OfflineEntityCommand;
import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.infra.property_management.property.sync.PropertySyncHandler;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PropertySyncHandler")
class PropertySyncHandlerTest {

    @Mock PropertyGateway propertyGateway;
    @Mock ProducerGateway producerGateway;
    @Mock SyncIdMappingGateway idMappingGateway;
    @Mock StorageGateway storageGateway;
    @InjectMocks PropertySyncHandler handler;

    @Test
    @DisplayName("deve resolver o produtor pelo mapeamento do batch (producerLocalId)")
    void shouldResolveProducerFromBatch() {
        final var producerLocalId = UUID.randomUUID();
        final var producerServerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var context = context();
        context.registerLocalMapping(producerLocalId, producerServerId);

        when(propertyGateway.save(any())).thenReturn(stubProperty(producerServerId, cityId));

        final var result = handler.handle(propertyCommand(UUID.randomUUID(), producerLocalId, cityId), context);

        assertEquals(SyncEntityStatus.CREATED, result.status());
    }

    @Test
    @DisplayName("deve resolver o produtor por producerId direto existente no servidor")
    void shouldResolveProducerByDirectId() {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();

        when(producerGateway.existsById(ProducerID.from(producerId))).thenReturn(true);
        when(propertyGateway.save(any())).thenReturn(stubProperty(producerId, cityId));

        final var result = handler.handle(propertyWithDirectProducer(UUID.randomUUID(), producerId, cityId), context());

        assertEquals(SyncEntityStatus.CREATED, result.status());
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException quando referência ao produtor é inválida")
    void shouldThrowForInvalidProducerReference() {
        final var command = propertyCommand(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        final var context = context();

        assertThrows(UnprocessableEntityException.class, () -> handler.handle(command, context));
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException quando producerId direto não existe no servidor")
    void shouldThrowWhenDirectProducerIdDoesNotExist() {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        when(producerGateway.existsById(ProducerID.from(producerId))).thenReturn(false);

        final var command = propertyWithDirectProducer(UUID.randomUUID(), producerId, cityId);

        assertThrows(UnprocessableEntityException.class, () -> handler.handle(command, context()));
    }

    @Test
    @DisplayName("deve declarar dependência de PRODUCER")
    void shouldDeclareProducerDependency() {
        assertEquals(java.util.Set.of(OfflineEntityType.PRODUCER), handler.dependencies());
    }

    @Test
    @DisplayName("deve resolver o produtor por mapeamento já persistido de batch anterior")
    void shouldResolveProducerFromPersistedMapping() {
        final var producerLocalId = UUID.randomUUID();
        final var producerServerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var context = context();
        when(idMappingGateway.findServerId(context.technicianId(), producerLocalId))
                .thenReturn(java.util.Optional.of(producerServerId));
        when(propertyGateway.save(any())).thenReturn(stubProperty(producerServerId, cityId));

        final var result = handler.handle(propertyCommand(UUID.randomUUID(), producerLocalId, cityId), context);

        assertEquals(SyncEntityStatus.CREATED, result.status());
    }

    @Test
    @DisplayName("deve mapear technicianIds e collaborators do payload")
    void shouldMapTechnicianIdsAndCollaborators() {
        final var producerLocalId = UUID.randomUUID();
        final var producerServerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var extraTechnicianId = UserID.unique();
        final var context = context();
        context.registerLocalMapping(producerLocalId, producerServerId);
        when(propertyGateway.save(any())).thenReturn(stubProperty(producerServerId, cityId));

        final var command = new OfflineEntityCommand(
                OfflineEntityType.PROPERTY, UUID.randomUUID(),
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerLocalId", producerLocalId.toString()),
                        entry("cityId", cityId.toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0"),
                        entry("technicianIds", List.of(extraTechnicianId.id().toString())),
                        entry("collaborators", List.of(Map.of("name", "Carlos", "hoursPerDay", "8")))
                ));

        final var result = handler.handle(command, context);

        assertEquals(SyncEntityStatus.CREATED, result.status());
    }

    @Test
    @DisplayName("deve armazenar anexo válido e vinculá-lo à propriedade")
    void shouldStoreValidAttachment() {
        final var producerLocalId = UUID.randomUUID();
        final var producerServerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var context = context();
        context.registerLocalMapping(producerLocalId, producerServerId);
        when(propertyGateway.save(any())).thenReturn(stubProperty(producerServerId, cityId));

        final var content = Base64.getEncoder().encodeToString("conteudo".getBytes());
        final var command = propertyCommandWithAttachment(producerLocalId, cityId, "image/png", content);

        final var result = handler.handle(command, context);

        assertEquals(SyncEntityStatus.CREATED, result.status());
        verify(storageGateway).store(any(), org.mockito.ArgumentMatchers.eq("image/png"), any());
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException para anexo com tipo de arquivo não suportado")
    void shouldThrowForDisallowedAttachmentContentType() {
        final var producerLocalId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var context = context();
        context.registerLocalMapping(producerLocalId, UUID.randomUUID());

        final var content = Base64.getEncoder().encodeToString("conteudo".getBytes());
        final var command = propertyCommandWithAttachment(producerLocalId, cityId, "application/zip", content);

        assertThrows(UnprocessableEntityException.class, () -> handler.handle(command, context));
    }

    @Test
    @DisplayName("deve lançar UnprocessableEntityException para anexo que excede o tamanho máximo")
    void shouldThrowForOversizedAttachment() {
        final var producerLocalId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var context = context();
        context.registerLocalMapping(producerLocalId, UUID.randomUUID());

        final var content = Base64.getEncoder().encodeToString(new byte[10 * 1024 * 1024 + 1]);
        final var command = propertyCommandWithAttachment(producerLocalId, cityId, "image/png", content);

        assertThrows(UnprocessableEntityException.class, () -> handler.handle(command, context));
    }

    private OfflineEntityCommand propertyCommandWithAttachment(
            final UUID producerLocalId, final UUID cityId, final String contentType, final String base64Content) {
        return new OfflineEntityCommand(
                OfflineEntityType.PROPERTY, UUID.randomUUID(),
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerLocalId", producerLocalId.toString()),
                        entry("cityId", cityId.toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0"),
                        entry("attachments", List.of(Map.of(
                                "fileName", "foto.png",
                                "contentType", contentType,
                                "base64", base64Content)))
                ));
    }

    private SyncContext context() {
        return new SyncContext(UserID.unique(), idMappingGateway);
    }

    private OfflineEntityCommand propertyCommand(
            final UUID localId, final UUID producerLocalId, final UUID cityId) {
        return new OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerLocalId", producerLocalId.toString()),
                        entry("cityId", cityId.toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0")));
    }

    private OfflineEntityCommand propertyWithDirectProducer(
            final UUID localId, final UUID producerId, final UUID cityId) {
        return new OfflineEntityCommand(
                OfflineEntityType.PROPERTY, localId,
                Map.ofEntries(
                        entry("name", "Fazenda Teste"),
                        entry("producerId", producerId.toString()),
                        entry("cityId", cityId.toString()),
                        entry("latitude", "0.0"),
                        entry("longitude", "0.0")));
    }

    private Property stubProperty(final UUID producerId, final UUID cityId) {
        return Property.with(
                PropertyID.unique(), "Fazenda Teste",
                Coord.from(BigDecimal.ZERO, BigDecimal.ZERO),
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                ProducerID.from(producerId),
                CityID.from(cityId),
                List.of(), List.of(), null, null);
    }
}
