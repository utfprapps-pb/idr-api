package br.gov.pr.idr.application.iam.refresh_token.issue;

import java.util.UUID;

public record IssueRefreshTokenCommand(UUID userId, String username, long expirationDays) {

    public static IssueRefreshTokenCommand from(final UUID userId, final String username, final long expirationDays) {
        return new IssueRefreshTokenCommand(userId, username, expirationDays);
    }
}
