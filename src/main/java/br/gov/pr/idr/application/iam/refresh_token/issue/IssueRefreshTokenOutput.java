package br.gov.pr.idr.application.iam.refresh_token.issue;

import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;

public record IssueRefreshTokenOutput(String refreshToken) {

    public static IssueRefreshTokenOutput from(final RefreshToken refreshToken) {
        return new IssueRefreshTokenOutput(refreshToken.getToken());
    }
}
