package br.gov.pr.idr.domain.livestock.animal;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalID(UUID id) implements Identifier {

    public AnimalID {
        Objects.requireNonNull(id, "AnimalID não pode ser nulo");
    }

    public static AnimalID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalID from(final UUID id) {
        return new AnimalID(id);
    }
}
