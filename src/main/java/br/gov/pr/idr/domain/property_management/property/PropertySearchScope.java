package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;

import java.util.Set;

public record PropertySearchScope(
        Type type,
        Set<RegionID> regionIds,
        Set<CityID> cityIds,
        UserID technicianId
) {

    public enum Type {UNRESTRICTED, BY_LOCATION, BY_TECHNICIAN}

    public PropertySearchScope {
        regionIds = regionIds == null ? Set.of() : Set.copyOf(regionIds);
        cityIds = cityIds == null ? Set.of() : Set.copyOf(cityIds);
    }

    public static PropertySearchScope unrestricted() {
        return new PropertySearchScope(Type.UNRESTRICTED, Set.of(), Set.of(), null);
    }

    public static PropertySearchScope byLocation(final Set<RegionID> regionIds, final Set<CityID> cityIds) {
        return new PropertySearchScope(Type.BY_LOCATION, regionIds, cityIds, null);
    }

    public static PropertySearchScope byTechnician(final UserID technicianId) {
        return new PropertySearchScope(Type.BY_TECHNICIAN, Set.of(), Set.of(), technicianId);
    }

    public boolean hasNoLocation() {
        return regionIds.isEmpty() && cityIds.isEmpty();
    }
}
