package br.gov.pr.idr.infra.property_management.city.persistence;

import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CityJPARepository extends JpaRepository<CityJPAEntity, UUID> {

    boolean existsByRegionId(UUID regionId);

    @Query("SELECT new br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult(c.id, c.name, c.state, " +
            "r.id, r.description) " +
            "FROM City c " +
            "LEFT JOIN RegionJPAEntity r ON c.regionId = r.id " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :terms, '%'))")
    Page<ListCityQueryResult> search(@Param("terms") String terms, Pageable pageable);
}
