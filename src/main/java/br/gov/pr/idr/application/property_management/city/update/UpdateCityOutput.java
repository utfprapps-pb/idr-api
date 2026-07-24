package br.gov.pr.idr.application.property_management.city.update;

import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.vo.State;

import java.util.UUID;

public record UpdateCityOutput(UUID id, String name, State state, UUID regionId) {

    public static UpdateCityOutput from(final City entity) {
        return new UpdateCityOutput(entity.getId().id(), entity.getName(), entity.getState(), entity.getRegionId().id());
    }
}
