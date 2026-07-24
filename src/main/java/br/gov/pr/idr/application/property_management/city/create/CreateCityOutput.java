package br.gov.pr.idr.application.property_management.city.create;

import br.gov.pr.idr.domain.property_management.city.City;

import java.util.UUID;

public record CreateCityOutput(UUID id, String name) {

    public static CreateCityOutput from(final City entity) {
        return new CreateCityOutput(entity.getId().id(), entity.getName());
    }
}
