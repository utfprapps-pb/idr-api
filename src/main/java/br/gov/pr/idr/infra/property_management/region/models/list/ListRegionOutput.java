package br.gov.pr.idr.infra.property_management.region.models.list;

import br.gov.pr.idr.domain.property_management.region.Region;

import java.util.UUID;

public record ListRegionOutput(UUID id, String description) {

    public static ListRegionOutput from(final Region region) {
        return new ListRegionOutput(region.getId().id(), region.getDescription());
    }
}
