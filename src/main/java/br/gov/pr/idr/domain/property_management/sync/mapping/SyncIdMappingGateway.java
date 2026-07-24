package br.gov.pr.idr.domain.property_management.sync.mapping;

import br.gov.pr.idr.domain.iam.user.UserID;

import java.util.Optional;
import java.util.UUID;

public interface SyncIdMappingGateway {

    boolean existsByTechnicianAndLocalId(final UserID technicianId, final UUID localId);

    Optional<UUID> findServerId(final UserID technicianId, final UUID localId);

    SyncIdMapping save(final SyncIdMapping mapping);
}
