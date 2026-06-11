package br.gov.pr.idr.infra.property_management.region.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegionJPARepository extends JpaRepository<RegionJPAEntity, UUID> {

    boolean existsByDescriptionIgnoreCase(final String description);
}
