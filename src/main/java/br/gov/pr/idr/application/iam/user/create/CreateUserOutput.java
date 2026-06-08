package br.gov.pr.idr.application.iam.user.create;

import br.gov.pr.idr.domain.iam.user.User;

import java.util.UUID;

public record CreateUserOutput(UUID id, String name) {

    public static CreateUserOutput from(final User entity) {
        return new CreateUserOutput(entity.getId().id(), entity.getName());
    }
}
