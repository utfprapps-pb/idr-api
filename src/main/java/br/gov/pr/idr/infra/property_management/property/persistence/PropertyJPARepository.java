package br.gov.pr.idr.infra.property_management.property.persistence;

import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PropertyJPARepository extends JpaRepository<PropertyJPAEntity, UUID> {

    @Query("SELECT p FROM Property p WHERE " +
            "(:terms = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%')))")
    Page<PropertyJPAEntity> search(@Param("terms") String terms, Pageable pageable);

    @Query("""
            SELECT new br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult(
                p.id, p.name, p.nakedAveragePrice, p.leaseAveragePrice,
                p.dairyCattleFarming, p.perennialPasture, p.summerPlowing, p.winterPlowing,
                p.latitude, p.longitude,
                p.version, p.updatedAt,
                producer.id, producer.name,
                city.id, city.name,
                (SELECT LISTAGG(CONCAT(cast(techId as String), '|', u.name), ',')
                 FROM Property ox JOIN ox.technicianIds techId, User u
                 WHERE ox.id = p.id AND u.id = techId),
                (SELECT LISTAGG(CONCAT(cast(c.id as String), '|', c.name, '|', c.hoursPerDay), ',')
                 FROM Property ox JOIN ox.collaborators c
                 WHERE ox.id = p.id)
            )
            FROM Property p
            LEFT JOIN Producer producer ON p.producerId = producer.id
            LEFT JOIN City city ON p.cityId = city.id
            WHERE p.id = :id
            """)
    Optional<GetPropertyQueryResult> findByIdWithDetails(@Param("id") UUID id);
}
