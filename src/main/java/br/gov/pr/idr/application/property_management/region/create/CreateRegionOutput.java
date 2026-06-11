package br.gov.pr.idr.application.property_management.region.create;

import br.gov.pr.idr.domain.property_management.region.Region;

import java.util.UUID;

public record CreateRegionOutput(UUID id, String description) {

    public static CreateRegionOutput from(final Region entity) {
        return new CreateRegionOutput(entity.getId().id(), entity.getDescription());
    }
}
