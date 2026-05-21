package br.gov.pr.idr.application.iam.user.retries;

import br.gov.pr.idr.domain.iam.user.User;

public record FindUserByUsernameOutput(
        Long id,
        String name
) {
    public static FindUserByUsernameOutput from(User user) {
        return new FindUserByUsernameOutput(
                user.getId().id(),
                user.getName()
        );
    }
}
