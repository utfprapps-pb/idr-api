package br.gov.pr.idr.domain.property_management.region;

import java.util.Optional;

public interface RegionGateway {

    boolean existsById(final RegionID id);

    Region save(final Region region);

    boolean existsByDescription(final String description);

    Optional<Region> findByID(final RegionID id);

    Region update(final Region region);

}
