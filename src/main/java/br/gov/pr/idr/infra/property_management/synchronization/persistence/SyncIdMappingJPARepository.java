package br.gov.pr.idr.infra.property_management.synchronization.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SyncIdMappingJPARepository extends JpaRepository<SyncIdMappingJPAEntity, SyncIdMappingId> {

    boolean existsByTechnicianIdAndLocalId(UUID technicianId, UUID localId);

    Optional<SyncIdMappingJPAEntity> findByTechnicianIdAndLocalId(UUID technicianId, UUID localId);
}
