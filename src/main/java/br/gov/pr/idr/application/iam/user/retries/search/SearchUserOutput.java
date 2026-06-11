package br.gov.pr.idr.application.iam.user.retries.search;

import br.gov.pr.idr.domain.iam.user.User;

public record SearchUserOutput(String name) {

    public static SearchUserOutput from(User user) {
        return new SearchUserOutput(user.getName());
    }
}
