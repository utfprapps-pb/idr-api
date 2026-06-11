package br.gov.pr.idr.domain.property_management.city;

import br.gov.pr.idr.domain.shared.Identifier;

import java.util.Objects;
import java.util.UUID;

public record CityID(UUID id) implements Identifier {

    public CityID {
        Objects.requireNonNull(id, "CityID não pode ser nulo");
    }

    public static CityID unique() {
        return from(UUID.randomUUID());
    }

    public static CityID from(final UUID id) {
        return new CityID(id);
    }
}
