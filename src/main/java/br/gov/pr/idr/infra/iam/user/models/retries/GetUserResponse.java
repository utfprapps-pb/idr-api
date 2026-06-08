package br.gov.pr.idr.infra.iam.user.models.retries;

public record GetUserResponse(String displayName) {

    public static GetUserResponse from(String username) {
        return new GetUserResponse(username);
    }
}
