package br.gov.pr.idr.domain.iam.refresh_token;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.UUID;

public record RefreshTokenID(UUID id) implements Identifier {

    public static RefreshTokenID from(final UUID id) {
        return new RefreshTokenID(id);
    }

    public static RefreshTokenID unique() {
        return from(UUID.randomUUID());
    }
}
