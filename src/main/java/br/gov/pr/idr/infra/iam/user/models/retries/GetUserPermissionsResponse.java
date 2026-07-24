package br.gov.pr.idr.infra.iam.user.models.retries;

import br.gov.pr.idr.application.iam.user.retries.permissions.GetUserPermissionsOutput;
import br.gov.pr.idr.domain.iam.user.UserRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record GetUserPermissionsResponse(
        UUID userId,
        List<PermissionItemResponse> permissions
) {

    public record PermissionItemResponse(
            UUID id,
            UserRole role,
            boolean readOnly,
            Set<UUID> regionIds,
            Set<UUID> cityIds
    ) {
    }

    public static GetUserPermissionsResponse from(final GetUserPermissionsOutput output) {
        final var permissions = output.permissions().stream()
                .map(permission -> new PermissionItemResponse(
                        permission.id(),
                        permission.role(),
                        permission.readOnly(),
                        permission.regionIds(),
                        permission.cityIds()
                ))
                .toList();
        return new GetUserPermissionsResponse(output.userId(), permissions);
    }
}
