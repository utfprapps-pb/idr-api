package br.gov.pr.idr.domain.property_management.sync.context;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMapping;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class SyncContext {

    private final UserID technicianId;
    private final SyncIdMappingGateway idMappingGateway;
    private final Map<UUID, UUID> batchLocalToServerId = new HashMap<>();

    public SyncContext(final UserID technicianId, final SyncIdMappingGateway idMappingGateway) {
        this.technicianId = Objects.requireNonNull(technicianId, "technicianId não pode ser nulo");
        this.idMappingGateway = Objects.requireNonNull(idMappingGateway, "idMappingGateway não pode ser nulo");
    }

    public UserID technicianId() {
        return technicianId;
    }

    public void registerLocalMapping(final UUID localId, final UUID serverId) {
        batchLocalToServerId.put(localId, serverId);
    }

    public Optional<UUID> resolveLocalMapping(final UUID localId) {
        return Optional.ofNullable(batchLocalToServerId.get(localId));
    }

    public boolean isAlreadySynced(final UUID localId) {
        return idMappingGateway.existsByTechnicianAndLocalId(technicianId, localId);
    }

    public Optional<UUID> findPersistedServerId(final UUID localId) {
        return idMappingGateway.findServerId(technicianId, localId);
    }

    public void persistMapping(final UUID localId, final UUID serverId, final OfflineEntityType entityType) {
        idMappingGateway.save(SyncIdMapping.create(technicianId, localId, serverId, entityType));
        registerLocalMapping(localId, serverId);
    }
}
