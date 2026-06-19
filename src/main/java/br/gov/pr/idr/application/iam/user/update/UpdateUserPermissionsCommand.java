package br.gov.pr.idr.application.iam.user.update;

import br.gov.pr.idr.domain.iam.user.UserRole;

import java.util.Set;
import java.util.UUID;

public record UpdateUserPermissionsCommand(
        UUID userId,
        UserRole role,
        boolean readOnly,
        Set<UUID> regionIds,
        Set<UUID> cityIds
) {

    public static UpdateUserPermissionsCommand from(final UUID userId, final UserRole role,
                                                    final boolean readOnly, final Set<UUID> regionIds,
                                                    final Set<UUID> cityIds) {
        return new UpdateUserPermissionsCommand(userId, role, readOnly, regionIds, cityIds);
    }
}
