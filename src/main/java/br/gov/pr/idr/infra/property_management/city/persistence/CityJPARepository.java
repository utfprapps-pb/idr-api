package br.gov.pr.idr.infra.property_management.city.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CityJPARepository extends JpaRepository<CityJPAEntity, Long> {
}
