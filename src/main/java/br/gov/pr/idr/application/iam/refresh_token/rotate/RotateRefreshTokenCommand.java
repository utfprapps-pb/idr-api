package br.gov.pr.idr.application.iam.refresh_token.rotate;

public record RotateRefreshTokenCommand(String token, long expirationDays) {

    public static RotateRefreshTokenCommand from(final String token, final long expirationDays) {
        return new RotateRefreshTokenCommand(token, expirationDays);
    }
}
