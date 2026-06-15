package br.gov.pr.idr.infra.property_management.city.models.update;

import br.gov.pr.idr.application.property_management.city.update.UpdateCityOutput;
import br.gov.pr.idr.domain.property_management.city.vo.State;

import java.util.UUID;

public record UpdateCityResponse(UUID id, String name, State state, UUID regionId) {

    public static UpdateCityResponse from(final UpdateCityOutput output) {
        return new UpdateCityResponse(output.id(), output.name(), output.state(), output.regionId());
    }
}
