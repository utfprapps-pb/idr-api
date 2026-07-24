package br.gov.pr.idr.application.property_management.city.update;

import java.util.UUID;

public record UpdateCityCommand(
        UUID cityId,
        UUID regionId,
        String name
) {

    public static UpdateCityCommand from(UUID cityId, UUID regionId, String name) {
        return new UpdateCityCommand(cityId, regionId, name);
    }
}
