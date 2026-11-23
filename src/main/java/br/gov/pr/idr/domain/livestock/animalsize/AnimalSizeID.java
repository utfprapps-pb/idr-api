package br.gov.pr.idr.domain.livestock.animalsize;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalSizeID(UUID id) implements Identifier {

    public AnimalSizeID {
        Objects.requireNonNull(id, "AnimalSizeID não pode ser nulo");
    }

    public static AnimalSizeID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalSizeID from(final UUID id) {
        return new AnimalSizeID(id);
    }

}
