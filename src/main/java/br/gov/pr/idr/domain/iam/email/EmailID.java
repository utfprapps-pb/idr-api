package br.gov.pr.idr.domain.iam.email;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.UUID;

public record EmailID(UUID id) implements Identifier {

    public static EmailID from(final UUID id) {
        return new EmailID(id);
    }

    public static EmailID unique() {
        return from(UUID.randomUUID());
    }
}
