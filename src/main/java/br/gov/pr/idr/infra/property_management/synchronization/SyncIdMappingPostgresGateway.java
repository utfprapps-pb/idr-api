package br.gov.pr.idr.infra.property_management.synchronization;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMapping;
import br.gov.pr.idr.domain.property_management.sync.mapping.SyncIdMappingGateway;
import br.gov.pr.idr.infra.property_management.synchronization.persistence.SyncIdMappingJPAEntity;
import br.gov.pr.idr.infra.property_management.synchronization.persistence.SyncIdMappingJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SyncIdMappingPostgresGateway implements SyncIdMappingGateway {

    private final SyncIdMappingJPARepository repository;

    @Override
    public boolean existsByTechnicianAndLocalId(final UserID technicianId, final UUID localId) {
        return repository.existsByTechnicianIdAndLocalId(technicianId.id(), localId);
    }

    @Override
    public Optional<UUID> findServerId(final UserID technicianId, final UUID localId) {
        return repository.findByTechnicianIdAndLocalId(technicianId.id(), localId)
                .map(SyncIdMappingJPAEntity::getServerId);
    }

    @Override
    public SyncIdMapping save(final SyncIdMapping mapping) {
        return repository.save(SyncIdMappingJPAEntity.from(mapping)).toDomain();
    }
}
