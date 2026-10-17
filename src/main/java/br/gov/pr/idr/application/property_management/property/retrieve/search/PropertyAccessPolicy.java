package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.region.RegionID;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class PropertyAccessPolicy {

    private PropertyAccessPolicy() {
    }

    public static PropertySearchScope resolve(final User user) {
        final var role = user.getPermissions().stream()
                .map(Permission::getRole)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (role == null) {
            return PropertySearchScope.byTechnician(user.getId());
        }

        return switch (role) {
            case ADMIN, COORDENACAO_GERAL -> PropertySearchScope.unrestricted();
            case GERENCIA_MACRO, GERENCIA_REGIONAL, GERENCIA_MUNICIPAL ->
                    PropertySearchScope.byLocation(regionIdsOf(user), cityIdsOf(user));
            case TECNICO -> PropertySearchScope.byTechnician(user.getId());
        };
    }

    public static boolean allows(final PropertySearchScope scope, final Property property, final CityGateway cityGateway) {
        return switch (scope.type()) {
            case UNRESTRICTED -> true;
            case BY_TECHNICIAN -> property.getTechnicianIds().contains(scope.technicianId());
            case BY_LOCATION -> scope.cityIds().contains(property.getCityId())
                    || cityGateway.findById(property.getCityId())
                            .map(city -> scope.regionIds().contains(city.getRegionId()))
                            .orElse(false);
        };
    }

    private static Set<RegionID> regionIdsOf(final User user) {
        return user.getPermissions().stream()
                .flatMap(p -> p.getRegionIds().stream())
                .map(RegionID::from)
                .collect(Collectors.toSet());
    }

    private static Set<CityID> cityIdsOf(final User user) {
        return user.getPermissions().stream()
                .flatMap(p -> p.getCityIds().stream())
                .map(CityID::from)
                .collect(Collectors.toSet());
    }
}
