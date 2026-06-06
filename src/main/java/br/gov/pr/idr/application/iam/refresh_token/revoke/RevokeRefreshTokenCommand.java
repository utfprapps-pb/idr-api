package br.gov.pr.idr.application.iam.refresh_token.revoke;

import java.util.UUID;

public record RevokeRefreshTokenCommand(UUID userId) {

    public static RevokeRefreshTokenCommand from(final UUID userId) {
        return new RevokeRefreshTokenCommand(userId);
    }
}
