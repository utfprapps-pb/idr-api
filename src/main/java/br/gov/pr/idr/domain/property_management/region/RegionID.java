package br.gov.pr.idr.domain.property_management.region;

import br.gov.pr.idr.domain.shared.Identifier;

import java.util.Objects;
import java.util.UUID;

public record RegionID(UUID id) implements Identifier {

    public RegionID {
        Objects.requireNonNull(id, "RegionID não pode ser nulo");
    }

    public static RegionID unique() {
        return from(UUID.randomUUID());
    }

    public static RegionID from(final UUID id) {
        return new RegionID(id);
    }

}
