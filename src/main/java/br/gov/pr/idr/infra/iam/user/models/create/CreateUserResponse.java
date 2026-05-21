package br.gov.pr.idr.infra.iam.user.models.create;

import br.gov.pr.idr.application.iam.user.create.CreateUserOutput;

public record CreateUserResponse(
        Long id,
        String name
) {

    public static CreateUserResponse from(final CreateUserOutput output) {
        return new CreateUserResponse(output.id(), output.name());
    }
}
