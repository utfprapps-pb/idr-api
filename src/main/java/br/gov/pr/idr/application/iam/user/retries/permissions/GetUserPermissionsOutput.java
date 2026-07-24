package br.gov.pr.idr.application.iam.user.retries.permissions;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record GetUserPermissionsOutput(
        UUID userId,
        List<PermissionItem> permissions
) {

    public record PermissionItem(
            UUID id,
            UserRole role,
            boolean readOnly,
            Set<UUID> regionIds,
            Set<UUID> cityIds
    ) {

        public static PermissionItem from(final Permission permission) {
            return new PermissionItem(
                    permission.getId().id(),
                    permission.getRole(),
                    permission.isReadOnly(),
                    permission.getRegionIds(),
                    permission.getCityIds()
            );
        }
    }

    public static GetUserPermissionsOutput from(final User user) {
        final var permissions = user.getPermissions().stream()
                .map(PermissionItem::from)
                .toList();
        return new GetUserPermissionsOutput(user.getId().id(), permissions);
    }
}
