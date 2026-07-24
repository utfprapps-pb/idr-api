package br.gov.pr.idr.infra.iam.user.models.retries;

import br.gov.pr.idr.application.iam.user.retries.find.FindUserByIdOutput;
import br.gov.pr.idr.domain.iam.user.UserRole;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record GetUserByIdResponse(
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

    public static GetUserByIdResponse from(final FindUserByIdOutput output) {
        return new GetUserByIdResponse(
                output.id(),
                output.name(),
                output.username(),
                output.cpf(),
                output.phone(),
                output.cityId(),
                output.active(),
                output.createdAt(),
                output.role(),
                output.readOnly(),
                output.regionIds(),
                output.cityIds()
        );
    }
}
