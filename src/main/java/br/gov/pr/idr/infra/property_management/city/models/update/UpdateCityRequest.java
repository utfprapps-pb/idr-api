package br.gov.pr.idr.infra.property_management.city.models.update;

import java.util.UUID;

public record UpdateCityRequest (UUID regionId, String name) {

    public static UpdateCityRequest from(final UUID regionId, final String name) {
        return new UpdateCityRequest(regionId, name);
    }

}
