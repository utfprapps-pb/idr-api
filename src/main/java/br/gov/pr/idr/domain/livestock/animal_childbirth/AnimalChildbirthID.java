package br.gov.pr.idr.domain.livestock.animal_childbirth;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalChildbirthID(UUID id) implements Identifier {

    public AnimalChildbirthID {
        Objects.requireNonNull(id, "AnimalChildbirthID não pode ser nulo");
    }

    public static AnimalChildbirthID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalChildbirthID from(final UUID id) {
        return new AnimalChildbirthID(id);
    }
}
