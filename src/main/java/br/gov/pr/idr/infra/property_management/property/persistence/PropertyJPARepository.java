package br.gov.pr.idr.infra.property_management.property.persistence;

import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PropertyJPARepository extends JpaRepository<PropertyJPAEntity, UUID> {

    @Query("SELECT p FROM Property p WHERE " +
            "(:terms = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%')))")
    Page<PropertyJPAEntity> search(@Param("terms") String terms, Pageable pageable);

    @Query("SELECT p FROM Property p LEFT JOIN City c ON p.cityId = c.id WHERE " +
            "(:terms = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%'))) AND " +
            "(c.regionId IN :regionIds OR p.cityId IN :cityIds)")
    Page<PropertyJPAEntity> searchByLocation(@Param("terms") String terms,
                                             @Param("regionIds") Set<UUID> regionIds,
                                             @Param("cityIds") Set<UUID> cityIds,
                                             Pageable pageable);

    @Query("SELECT p FROM Property p WHERE " +
            "(:terms = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%'))) AND " +
            ":technicianId MEMBER OF p.technicianIds")
    Page<PropertyJPAEntity> searchByTechnician(@Param("terms") String terms,
                                               @Param("technicianId") UUID technicianId,
                                               Pageable pageable);

    @Query("""
            SELECT new br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult(
                p.id, p.name, p.nakedAveragePrice, p.leaseAveragePrice,
                p.dairyCattleFarming, p.perennialPasture, p.summerPlowing, p.winterPlowing,
                p.latitude, p.longitude,
                p.version, p.updatedAt,
                producer.id, producer.name,
                city.id, city.name,
                (SELECT LISTAGG(CONCAT(cast(techId as String), '\u001F', u.name), '\u001E')
                 FROM Property ox JOIN ox.technicianIds techId, User u
                 WHERE ox.id = p.id AND u.id = techId),
                (SELECT LISTAGG(CONCAT(cast(c.id as String), '\u001F', c.name, '\u001F', c.hoursPerDay), '\u001E')
                 FROM Property ox JOIN ox.collaborators c
                 WHERE ox.id = p.id),
                (SELECT LISTAGG(CONCAT(cast(a.id as String), '\u001F', a.fileName, '\u001F', a.contentType, '\u001F',\s
                            cast(a.sizeBytes as String)), '\u001E')
                 FROM Property ox JOIN ox.attachments a
                 WHERE ox.id = p.id)
            )
            FROM Property p
            LEFT JOIN Producer producer ON p.producerId = producer.id
            LEFT JOIN City city ON p.cityId = city.id
            WHERE p.id = :id
           \s""")
    Optional<GetPropertyQueryResult> findByIdWithDetails(@Param("id") UUID id);
}
