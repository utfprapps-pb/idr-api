package br.gov.pr.idr.domain.livestock.mastitis;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record MastitisID(UUID id) implements Identifier {

    public MastitisID {
        Objects.requireNonNull(id, "MastitisID não pode ser nulo");
    }

    public static MastitisID unique() {
        return from(UUID.randomUUID());
    }

    public static MastitisID from(final UUID id) {
        return new MastitisID(id);
    }
}
