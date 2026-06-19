package br.gov.pr.idr.infra.iam.user.models.retries;

import br.gov.pr.idr.application.iam.user.retries.search.SearchUserOutput;
import br.gov.pr.idr.domain.iam.user.UserRole;

import java.time.Instant;
import java.util.UUID;

public record SearchUserResponse(
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

    public static SearchUserResponse from(final SearchUserOutput output) {
        return new SearchUserResponse(
                output.id(),
                output.name(),
                output.username(),
                output.phone(),
                output.professionalRegister(),
                output.graduationYear(),
                output.cityId(),
                output.active(),
                output.createdAt(),
                output.role()
        );
    }
}
