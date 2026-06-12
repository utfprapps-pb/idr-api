package br.gov.pr.idr.infra.property_management.property.models;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyOutput;

import java.util.UUID;

public record CreatePropertyResponse(UUID id, String name) {

    public static CreatePropertyResponse from(final CreatePropertyOutput output) {
        return new CreatePropertyResponse(output.id(), output.name());
    }
}
