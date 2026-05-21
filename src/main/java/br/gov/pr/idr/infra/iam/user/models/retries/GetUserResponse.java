package br.gov.pr.idr.infra.iam.user.models.retries;

import br.gov.pr.idr.application.iam.user.retries.FindUserByUsernameOutput;

public record GetUserResponse(Long id, String displayName) {

    public static GetUserResponse from(FindUserByUsernameOutput out) {
        return new GetUserResponse(out.id(), out.name());
    }
}
