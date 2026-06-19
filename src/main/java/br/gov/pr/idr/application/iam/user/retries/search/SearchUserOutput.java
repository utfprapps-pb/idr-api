package br.gov.pr.idr.application.iam.user.retries.search;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserRole;

import java.time.Instant;
import java.util.UUID;

public record SearchUserOutput(
        UUID id,
        String name,
        String username,
        String phone,
        String professionalRegister,
        String graduationYear,
        UUID cityId,
        boolean active,
        Instant createdAt,
        UserRole role
) {

    public static SearchUserOutput from(final User user) {
        final var permission = user.getPermissions().stream().findFirst().orElse(null);
        return new SearchUserOutput(
                user.getId().id(),
                user.getName(),
                user.getUsername(),
                user.getPhone(),
                user.getProfessionalRegister(),
                user.getGraduationYear(),
                user.getCityID().id(),
                user.isActive(),
                user.getCreatedAt(),
                permission != null ? permission.getRole() : null
        );
    }
}
