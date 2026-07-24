package br.gov.pr.idr.application.iam.user.retries.find;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserRole;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record FindUserByIdOutput(
        UUID id,
        String name,
        String username,
        String cpf,
        String phone,
        UUID cityId,
        boolean active,
        Instant createdAt,
        UserRole role,
        boolean readOnly,
        Set<UUID> regionIds,
        Set<UUID> cityIds
) {

    public static FindUserByIdOutput from(final User user) {
        final var permission = user.getPermissions().stream().findFirst().orElse(null);
        return new FindUserByIdOutput(
                user.getId().id(),
                user.getName(),
                user.getUsername(),
                user.getCpf().value(),
                user.getPhone(),
                user.getCityID().id(),
                user.isActive(),
                user.getCreatedAt(),
                permission != null ? permission.getRole() : null,
                permission != null && permission.isReadOnly(),
                permission != null ? permission.getRegionIds() : Set.of(),
                permission != null ? permission.getCityIds() : Set.of()
        );
    }
}
