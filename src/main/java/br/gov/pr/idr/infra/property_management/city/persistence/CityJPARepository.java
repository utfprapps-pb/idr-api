package br.gov.pr.idr.infra.property_management.city.persistence;

import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CityJPARepository extends JpaRepository<CityJPAEntity, UUID> {

    boolean existsByRegionId(UUID regionId);

    List<CityJPAEntity> findAllByRegionIdIn(Set<UUID> regionIds);

    List<CityJPAEntity> findAllByRegionIdInAndUpdatedAtAfter(Set<UUID> regionIds, Instant since);

    @Query(value = "SELECT * FROM city WHERE region_id IN (:regionIds) AND deleted_at IS NOT NULL AND deleted_at > :since",
            nativeQuery = true)
    List<CityJPAEntity> findDeletedSince(@Param("regionIds") Set<UUID> regionIds, @Param("since") Instant since);

    List<CityJPAEntity> findAllByIdInAndUpdatedAtAfter(Set<UUID> ids, Instant since);

    @Query(value = "SELECT * FROM city WHERE id IN (:ids) AND deleted_at IS NOT NULL AND deleted_at > :since",
            nativeQuery = true)
    List<CityJPAEntity> findDeletedByIdsSince(@Param("ids") Set<UUID> ids, @Param("since") Instant since);

    @Query("SELECT new br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult(c.id, c.name, c.state, " +
            "r.id, r.description) " +
            "FROM City c " +
            "LEFT JOIN RegionJPAEntity r ON c.regionId = r.id " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :terms, '%'))")
    Page<ListCityQueryResult> search(@Param("terms") String terms, Pageable pageable);
}
