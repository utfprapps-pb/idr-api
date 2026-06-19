package br.gov.pr.idr.infra.property_management.property.models.update;

import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyOutput;

import java.util.UUID;

public record UpdatePropertyResponse(UUID id, String name) {

    public static UpdatePropertyResponse from(final UpdatePropertyOutput output) {
        return new UpdatePropertyResponse(output.id(), output.name());
    }
}
