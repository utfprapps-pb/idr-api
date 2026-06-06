package br.gov.pr.idr.application.iam.refresh_token.rotate;

import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;

public record RotateRefreshTokenOutput(String username, String newRefreshToken) {

    public static RotateRefreshTokenOutput from(final String username, final RefreshToken newToken) {
        return new RotateRefreshTokenOutput(username, newToken.getToken());
    }
}
