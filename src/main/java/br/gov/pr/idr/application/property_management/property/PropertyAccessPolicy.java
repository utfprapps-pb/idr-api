package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.region.RegionID;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class PropertyAccessPolicy {

    private PropertyAccessPolicy() {
    }

    public static PropertySearchScope resolve(final User user) {
        return resolve(user.getId(), user.getPermissions());
    }

    /** Mesmo critério de {@link #resolve(User)}, a partir só do id e das permissões (sem montar o usuário). */
    public static PropertySearchScope resolve(final UserID userId, final Collection<Permission> permissions) {
        final var role = permissions.stream()
                .map(Permission::getRole)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (role == null) {
            return PropertySearchScope.byTechnician(userId);
        }

        return switch (role) {
            case ADMIN, COORDENACAO_GERAL -> PropertySearchScope.unrestricted();
            case GERENCIA_MACRO, GERENCIA_REGIONAL, GERENCIA_MUNICIPAL ->
                    PropertySearchScope.byLocation(regionIdsOf(permissions), cityIdsOf(permissions));
            case TECNICO -> PropertySearchScope.byTechnician(userId);
        };
    }

    private static Set<RegionID> regionIdsOf(final Collection<Permission> permissions) {
        return permissions.stream()
                .flatMap(p -> p.getRegionIds().stream())
                .map(RegionID::from)
                .collect(Collectors.toSet());
    }

    private static Set<CityID> cityIdsOf(final Collection<Permission> permissions) {
        return permissions.stream()
                .flatMap(p -> p.getCityIds().stream())
                .map(CityID::from)
                .collect(Collectors.toSet());
    }
}
