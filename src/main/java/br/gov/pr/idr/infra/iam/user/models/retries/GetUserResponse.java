package br.gov.pr.idr.infra.iam.user.models.retries;

public record GetUserResponse(String displayName, String role) {

    public static GetUserResponse from(String username, String role) {
        return new GetUserResponse(username, role);
    }
}
