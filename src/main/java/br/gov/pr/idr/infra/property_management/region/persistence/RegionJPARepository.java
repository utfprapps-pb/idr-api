package br.gov.pr.idr.infra.property_management.region.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RegionJPARepository extends JpaRepository<RegionJPAEntity, UUID>,
        JpaSpecificationExecutor<RegionJPAEntity> {

    boolean existsByDescription(final String description);

    List<RegionJPAEntity> findAllByIdIn(Set<UUID> ids);
}
