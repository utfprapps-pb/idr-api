package br.gov.pr.idr.domain.livestock.animal_diseases;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalDiseasesID(UUID id) implements Identifier {

    public AnimalDiseasesID {
        Objects.requireNonNull(id, "AnimalDiseasesID não pode ser nulo");
    }

    public static AnimalDiseasesID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalDiseasesID from(final UUID id) {
        return new AnimalDiseasesID(id);
    }
}
