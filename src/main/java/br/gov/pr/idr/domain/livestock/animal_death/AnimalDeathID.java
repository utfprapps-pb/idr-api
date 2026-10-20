package br.gov.pr.idr.domain.livestock.animal_death;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalDeathID(UUID id) implements Identifier {

    public AnimalDeathID {
        Objects.requireNonNull(id, "AnimalDeathID não pode ser nulo");
    }

    public static AnimalDeathID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalDeathID from(final UUID id) {
        return new AnimalDeathID(id);
    }
}
