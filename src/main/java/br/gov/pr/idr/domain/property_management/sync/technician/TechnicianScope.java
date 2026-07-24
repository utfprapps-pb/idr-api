package br.gov.pr.idr.domain.property_management.sync.technician;

import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;

import java.util.Set;

public record TechnicianScope(
        Set<RegionID> regionIds,
        Set<CityID> cityIds
) {

    public TechnicianScope {
        regionIds = regionIds == null ? Set.of() : Set.copyOf(regionIds);
        cityIds = cityIds == null ? Set.of() : Set.copyOf(cityIds);
    }
}
