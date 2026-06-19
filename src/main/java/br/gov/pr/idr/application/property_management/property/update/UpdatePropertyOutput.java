package br.gov.pr.idr.application.property_management.property.update;

import br.gov.pr.idr.domain.property_management.property.Property;

import java.util.UUID;

public record UpdatePropertyOutput(UUID id, String name) {

    public static UpdatePropertyOutput from(final Property property) {
        return new UpdatePropertyOutput(property.getId().id(), property.getName());
    }
}
