package br.gov.pr.idr.infra.property_management.region.models.update;

import br.gov.pr.idr.application.property_management.region.update.UpdateRegionOutput;

import java.util.UUID;

public record UpdateRegionResponse(UUID id, String description) {

    public static UpdateRegionResponse from(final UpdateRegionOutput output) {
        return new UpdateRegionResponse(output.id(), output.description());
    }
}
