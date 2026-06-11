package br.gov.pr.idr.application.property_management.city.create;

import br.gov.pr.idr.domain.property_management.city.vo.State;

import java.util.UUID;

public record CreateCityCommand(
        String name,
        State state,
        UUID regionId
) {
}
