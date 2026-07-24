package br.gov.pr.idr.infra.property_management.region.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RegionJPARepository extends JpaRepository<RegionJPAEntity, UUID>,
        JpaSpecificationExecutor<RegionJPAEntity> {

    boolean existsByDescription(final String description);

    List<RegionJPAEntity> findAllByIdIn(Set<UUID> ids);

    List<RegionJPAEntity> findAllByIdInAndUpdatedAtAfter(Set<UUID> ids, Instant since);

    @Query(value = "SELECT * FROM region WHERE id IN (:ids) AND deleted_at IS NOT NULL AND deleted_at > :since",
            nativeQuery = true)
    List<RegionJPAEntity> findDeletedSince(@Param("ids") Set<UUID> ids, @Param("since") Instant since);
}
