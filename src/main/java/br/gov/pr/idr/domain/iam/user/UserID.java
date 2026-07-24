package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.UUID;

public record UserID(UUID id) implements Identifier {

    public static UserID from(final UUID id) {
        return new UserID(id);
    }

    public static UserID unique(){
        return from(UUID.randomUUID());
    }
}
