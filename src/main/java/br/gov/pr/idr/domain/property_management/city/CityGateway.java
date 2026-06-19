package br.gov.pr.idr.domain.property_management.city;

import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

import java.util.Optional;

public interface CityGateway {

    boolean existsById(CityID id);

    City save(final City city);

    Optional<City> findById(final CityID cityId);

    City update(final City city);

    Pagination<ListCityQueryResult> search(final SearchQuery query);

    void deleteById(final CityID id);

    boolean existsByRegionId(final RegionID regionId);
}
