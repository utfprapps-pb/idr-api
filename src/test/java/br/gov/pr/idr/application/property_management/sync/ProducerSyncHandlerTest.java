package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.domain.property_management.sync.entity.OfflineEntityCommand;
import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.infra.property_management.producer.sync.ProducerSyncHandler;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProducerSyncHandler")
class ProducerSyncHandlerTest {

    private static final String VALID_CPF = "529.982.247-25";

    @Mock ProducerGateway producerGateway;
    @Mock SyncIdMappingGateway idMappingGateway;
    @InjectMocks ProducerSyncHandler handler;

    @Test
    @DisplayName("deve criar produtor novo e persistir o mapeamento de idempotência")
    void shouldCreateNewProducer() {
        final var localId = UUID.randomUUID();
        final var saved = Producer.with(ProducerID.unique(), "João", CPF.from(VALID_CPF));

        when(producerGateway.findByCpf(any())).thenReturn(Optional.empty());
        when(producerGateway.save(any())).thenReturn(saved);

        final var result = handler.handle(producerCommand(localId), context());

        assertEquals(SyncEntityStatus.CREATED, result.status());
        assertEquals(saved.getId().id(), result.serverId());
        verify(producerGateway).save(any());
        verify(idMappingGateway).save(argThat(m ->
                m.localId().equals(localId)
                        && m.serverId().equals(saved.getId().id())
                        && m.entityType() == OfflineEntityType.PRODUCER));
    }

    @Test
    @DisplayName("deve retornar EXISTING para CPF já cadastrado sem criar novo produtor")
    void shouldReturnExistingForDuplicateCpf() {
        final var localId = UUID.randomUUID();
        final var existing = Producer.with(ProducerID.unique(), "José", CPF.from(VALID_CPF));

        when(producerGateway.findByCpf(any())).thenReturn(Optional.of(existing));

        final var result = handler.handle(producerCommand(localId), context());

        assertEquals(SyncEntityStatus.EXISTING, result.status());
        assertEquals(existing.getId().id(), result.serverId());
        verify(producerGateway, never()).save(any());
        verify(idMappingGateway).save(any());
    }

    @Test
    @DisplayName("deve retornar EXISTING via idempotência quando o localId já foi sincronizado")
    void shouldReturnExistingWhenAlreadySynced() {
        final var localId = UUID.randomUUID();
        final var serverId = UUID.randomUUID();
        final var context = context();

        when(idMappingGateway.existsByTechnicianAndLocalId(any(), eq(localId))).thenReturn(true);
        when(idMappingGateway.findServerId(any(), eq(localId))).thenReturn(Optional.of(serverId));

        final var result = handler.handle(producerCommand(localId), context);

        assertEquals(SyncEntityStatus.EXISTING, result.status());
        assertEquals(serverId, result.serverId());
        verifyNoInteractions(producerGateway);
        verify(idMappingGateway, never()).save(any());
    }

    private SyncContext context() {
        return new SyncContext(UserID.unique(), idMappingGateway);
    }

    private OfflineEntityCommand producerCommand(final UUID localId) {
        return new OfflineEntityCommand(
                OfflineEntityType.PRODUCER, localId, Map.of("name", "João", "cpf", VALID_CPF));
    }
}
