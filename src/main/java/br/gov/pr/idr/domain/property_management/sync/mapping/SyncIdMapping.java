package br.gov.pr.idr.domain.property_management.sync.mapping;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

public class SyncIdMapping extends AggregateRoot<SyncIdMappingID> {

    private final UserID technicianId;
    private final UUID localId;
    private final UUID serverId;
    private final OfflineEntityType entityType;
    private final Instant syncedAt;

    protected SyncIdMapping(final UserID technicianId, final UUID localId, final UUID serverId,
                             final OfflineEntityType entityType, final Instant syncedAt) {
        super(SyncIdMappingID.from(technicianId.id(), localId));
        this.technicianId = technicianId;
        this.localId = localId;
        this.serverId = serverId;
        this.entityType = entityType;
        this.syncedAt = syncedAt;
    }

    public static SyncIdMapping create(final UserID technicianId, final UUID localId,
                                        final UUID serverId, final OfflineEntityType entityType) {
        SyncIdMapping syncIdMapping = new SyncIdMapping(technicianId, localId, serverId, entityType, null);
        syncIdMapping.selfValidate();
        return syncIdMapping;
    }

    public static SyncIdMapping from(final UserID technicianId, final UUID localId, final UUID serverId,
                                      final OfflineEntityType entityType, final Instant syncedAt) {
        SyncIdMapping syncIdMapping = new SyncIdMapping(technicianId, localId, serverId, entityType, syncedAt);
        syncIdMapping.selfValidate();
        return syncIdMapping;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (this.serverId == null) {
            handler.append(DomainError.from("serverId", "serverId não pode ser nulo"));
        }
        if (this.entityType == null) {
            handler.append(DomainError.from("entityType", "entityType não pode ser nulo"));
        }
    }

    public UserID technicianId() {
        return technicianId;
    }

    public UUID localId() {
        return localId;
    }

    public UUID serverId() {
        return serverId;
    }

    public OfflineEntityType entityType() {
        return entityType;
    }

    public Instant syncedAt() {
        return syncedAt;
    }
}
