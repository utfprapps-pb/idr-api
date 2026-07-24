package br.gov.pr.idr.infra.property_management.region.models.create;

import br.gov.pr.idr.application.property_management.region.create.CreateRegionOutput;

import java.util.UUID;

public record CreateRegionResponse(UUID id, String description) {

    public static CreateRegionResponse from(final CreateRegionOutput output) {
        return new CreateRegionResponse(output.id(), output.description());
    }
}
