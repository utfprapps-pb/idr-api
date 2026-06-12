package br.gov.pr.idr.domain.property_management.property.collaborator;

import br.gov.pr.idr.domain.shared.Identifier;

import java.util.Objects;
import java.util.UUID;

public record PropertyCollaboratorID(UUID id) implements Identifier {

    public PropertyCollaboratorID {
        Objects.requireNonNull(id, "PropertyCollaboratorID cannot be null");
    }

    public static PropertyCollaboratorID from(final UUID id) {
        return new PropertyCollaboratorID(id);
    }

    public static PropertyCollaboratorID unique() {
        return from(UUID.randomUUID());
    }
}
