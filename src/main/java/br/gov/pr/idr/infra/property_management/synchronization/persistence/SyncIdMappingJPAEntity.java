package br.gov.pr.idr.infra.property_management.synchronization.persistence;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMapping;
import br.gov.pr.idr.domain.property_management.sync.vo.OfflineEntityType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "SyncIdMapping")
@Table(name = "sync_id_mapping")
@IdClass(SyncIdMappingId.class)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SyncIdMappingJPAEntity {

    @Id
    @Column(name = "technician_id", nullable = false, updatable = false)
    private UUID technicianId;

    @Id
    @Column(name = "local_id", nullable = false, updatable = false)
    private UUID localId;

    @Column(name = "server_id", nullable = false, updatable = false)
    private UUID serverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, updatable = false)
    private OfflineEntityType entityType;

    @CreationTimestamp
    @Column(name = "synced_at", nullable = false, updatable = false)
    private Instant syncedAt;

    public static SyncIdMappingJPAEntity from(final SyncIdMapping entity) {
        return new SyncIdMappingJPAEntity(entity.technicianId().id(),
                                          entity.localId(),
                                          entity.serverId(),
                                          entity.entityType(),
                                          Instant.now());
    }

    public SyncIdMapping toDomain() {
        return SyncIdMapping.from(UserID.from(this.getTechnicianId()),
                                  this.getLocalId(),
                                  this.getServerId(),
                                  this.getEntityType(),
                                  this.getSyncedAt());
    }
}
