package br.gov.pr.idr.application.property_management.region.update;

import br.gov.pr.idr.domain.property_management.region.Region;

import java.util.UUID;

public record UpdateRegionOutput(UUID id, String description) {

    public static UpdateRegionOutput from(final Region entity) {
        return new UpdateRegionOutput(entity.getId().id(), entity.getDescription());
    }
}
