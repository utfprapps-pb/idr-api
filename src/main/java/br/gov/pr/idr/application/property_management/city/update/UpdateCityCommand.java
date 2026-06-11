package br.gov.pr.idr.application.property_management.city.update;

import java.util.UUID;

public record UpdateCityCommand(
        UUID cityId,
        UUID regionId
) {
}
