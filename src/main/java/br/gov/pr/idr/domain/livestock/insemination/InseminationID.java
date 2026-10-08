package br.gov.pr.idr.domain.livestock.insemination;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record InseminationID(UUID id) implements Identifier {

    public InseminationID {
        Objects.requireNonNull(id, "InseminationID não pode ser nulo");
    }

    public static InseminationID unique() {
        return from(UUID.randomUUID());
    }

    public static InseminationID from(final UUID id) {
        return new InseminationID(id);
    }
}
