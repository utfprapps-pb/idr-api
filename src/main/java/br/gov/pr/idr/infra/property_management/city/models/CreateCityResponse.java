package br.gov.pr.idr.infra.property_management.city.models;

import br.gov.pr.idr.application.property_management.city.create.CreateCityOutput;

import java.util.UUID;

public record CreateCityResponse(UUID id, String name) {

    public static CreateCityResponse from(final CreateCityOutput output) {
        return new CreateCityResponse(output.id(), output.name());
    }
}
