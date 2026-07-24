package br.gov.pr.idr.infra.iam.user.models.create;

import br.gov.pr.idr.application.iam.user.create.CreateUserOutput;

import java.util.UUID;

public record CreateUserResponse(
        UUID id,
        String name
) {

    public static CreateUserResponse from(final CreateUserOutput output) {
        return new CreateUserResponse(output.id(), output.name());
    }
}
