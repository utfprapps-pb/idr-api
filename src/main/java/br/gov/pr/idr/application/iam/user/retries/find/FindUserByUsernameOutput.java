package br.gov.pr.idr.application.iam.user.retries.find;

import br.gov.pr.idr.domain.iam.user.User;

import java.util.UUID;

public record FindUserByUsernameOutput(
        UUID id,
        String name
) {
    public static FindUserByUsernameOutput from(User user) {
        return new FindUserByUsernameOutput(
                user.getId().id(),
                user.getName()
        );
    }
}
