package br.gov.pr.idr.domain.livestock.breed;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record BreedID(UUID id) implements Identifier {

    public BreedID {
        Objects.requireNonNull(id, "BreedID não pode ser nulo");
    }

    public static BreedID unique() {
        return from(UUID.randomUUID());
    }

    public static BreedID from(final UUID id) {
        return new BreedID(id);
    }

}
