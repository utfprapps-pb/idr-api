package br.gov.pr.idr.domain.property_management.property.attachment;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record PropertyAttachmentID(UUID id) implements Identifier {

    public PropertyAttachmentID {
        Objects.requireNonNull(id, "PropertyAttachmentID cannot be null");
    }

    public static PropertyAttachmentID from(final UUID id) {
        return new PropertyAttachmentID(id);
    }

    public static PropertyAttachmentID unique() {
        return from(UUID.randomUUID());
    }
}
