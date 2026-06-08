package br.gov.pr.idr.infra.property_management.city.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityJPARepository extends JpaRepository<CityJPAEntity, UUID> {
}
