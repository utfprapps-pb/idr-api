package br.gov.pr.idr.infra.property_management.property.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PropertyJPARepository extends JpaRepository<PropertyJPAEntity, UUID> {

    @Query("SELECT p FROM Property p WHERE " +
            "(:terms = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%')))")
    Page<PropertyJPAEntity> search(@Param("terms") String terms, Pageable pageable);
}
