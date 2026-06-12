package br.gov.pr.idr.application.property_management.property.create;

import br.gov.pr.idr.domain.property_management.property.Property;

import java.util.UUID;

public record CreatePropertyOutput(UUID id, String name) {

    public static CreatePropertyOutput from(final Property entity) {
        return new CreatePropertyOutput(entity.getId().id(), entity.getName());
    }
}
