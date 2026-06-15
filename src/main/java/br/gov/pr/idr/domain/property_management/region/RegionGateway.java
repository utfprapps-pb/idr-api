package br.gov.pr.idr.domain.property_management.region;

import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

import java.util.Optional;

public interface RegionGateway {

    boolean existsById(final RegionID id);

    Region save(final Region region);

    boolean existsByDescription(final String description);

    Optional<Region> findByID(final RegionID id);

    Region update(final Region region);

    Pagination<Region> search(final SearchQuery query);

}
