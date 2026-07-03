package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record PropertyID(UUID id) implements Identifier {

    public PropertyID {
        Objects.requireNonNull(id, "PropertyID cannot be null");
    }

    public static PropertyID from(final UUID id) {
        return new PropertyID(id);
    }

    public static PropertyID unique() {
        return from(UUID.randomUUID());
    }
}
