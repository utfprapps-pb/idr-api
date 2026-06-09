package br.gov.pr.idr.infra.iam.user.models.auth;

public record AuthLoginResponse(String accessToken, String refreshToken) {

    public static AuthLoginResponse from(String accessToken, String refreshToken) {
        return new AuthLoginResponse(accessToken, refreshToken);
    }
}
