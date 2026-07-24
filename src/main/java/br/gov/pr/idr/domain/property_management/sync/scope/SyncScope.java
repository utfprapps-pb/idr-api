package br.gov.pr.idr.domain.property_management.sync.scope;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record SyncScope(
        UserID technicianId,
        Set<RegionID> regionIds,
        Set<CityID> cityIds,
        Instant since
) {

    public SyncScope {
        Objects.requireNonNull(technicianId, "technicianId não pode ser nulo");
        regionIds = regionIds == null ? Set.of() : Set.copyOf(regionIds);
        cityIds = cityIds == null ? Set.of() : Set.copyOf(cityIds);
    }

    public boolean isIncremental() {
        return since != null;
    }

    public boolean hasNoScope() {
        return regionIds.isEmpty() && cityIds.isEmpty();
    }

    public Set<UUID> regionUuids() {
        return regionIds.stream().map(RegionID::id).collect(Collectors.toSet());
    }

    public Set<UUID> cityUuids() {
        return cityIds.stream().map(CityID::id).collect(Collectors.toSet());
    }
}
