package br.gov.pr.idr.domain.property_management.property.technician;

import br.gov.pr.idr.domain.shared.Identifier;

import java.util.Objects;
import java.util.UUID;

public record PropertyTechnicianID(UUID id) implements Identifier {

    public PropertyTechnicianID {
        Objects.requireNonNull(id, "PropertyTechnicianID cannot be null");
    }

    public static PropertyTechnicianID from(final UUID id) {
        return new PropertyTechnicianID(id);
    }

    public static PropertyTechnicianID unique() {
        return from(UUID.randomUUID());
    }
}
