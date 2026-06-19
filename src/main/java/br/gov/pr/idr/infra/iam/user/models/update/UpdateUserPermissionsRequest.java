package br.gov.pr.idr.infra.iam.user.models.update;

import br.gov.pr.idr.domain.iam.user.UserRole;

import java.util.Set;
import java.util.UUID;

public record UpdateUserPermissionsRequest(
        UserRole role,
        boolean readOnly,
        Set<UUID> regionIds,
        Set<UUID> cityIds
) {
}
