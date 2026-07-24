package br.gov.pr.idr.infra.property_management.producer.sync;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.sync.context.SyncContext;
import br.gov.pr.idr.domain.property_management.sync.payload.SyncPayloadReader;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.entity.OfflineEntityCommand;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityHandler;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProducerSyncHandler implements SyncEntityHandler {

    private final ProducerGateway producerGateway;

    @Override
    public OfflineEntityType type() {
        return OfflineEntityType.PRODUCER;
    }

    @Override
    public SyncEntityResult handle(final OfflineEntityCommand command, final SyncContext context) {
        final var localId = command.localId();

        if (context.isAlreadySynced(localId)) {
            final var serverId = context.findPersistedServerId(localId).orElseThrow();
            context.registerLocalMapping(localId, serverId);
            return new SyncEntityResult(localId, serverId, SyncEntityStatus.EXISTING, null);
        }

        final var reader = SyncPayloadReader.of(command.data());
        final var cpf = CPF.from(reader.requiredString("cpf"));

        final var existing = producerGateway.findByCpf(cpf);
        if (existing.isPresent()) {
            final var serverId = existing.get().getId().id();
            context.persistMapping(localId, serverId, OfflineEntityType.PRODUCER);
            return new SyncEntityResult(localId, serverId, SyncEntityStatus.EXISTING, null);
        }

        final var name = reader.requiredString("name");
        final var saved = producerGateway.save(Producer.create(name, cpf));
        final var serverId = saved.getId().id();
        context.persistMapping(localId, serverId, OfflineEntityType.PRODUCER);
        return new SyncEntityResult(localId, serverId, SyncEntityStatus.CREATED, null);
    }
}
