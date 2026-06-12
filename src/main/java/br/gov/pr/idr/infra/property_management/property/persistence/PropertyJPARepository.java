package br.gov.pr.idr.infra.property_management.property.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyJPARepository extends JpaRepository<PropertyJPAEntity, UUID> {
}
