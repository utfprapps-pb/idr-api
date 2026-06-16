package br.gov.pr.idr.domain.property_management.city.query;

import br.gov.pr.idr.domain.property_management.city.vo.State;

import java.util.UUID;

public record ListCityQueryResult(UUID id,
                                  String name,
                                  State state,
                                  Region region) {

    public record Region(UUID id, String name) {
    }

    public ListCityQueryResult(UUID id, String name, State state, UUID regionId, String regionName) {
        final var formattedName = state != null ? name + " - " + state : name;
        this(id, formattedName, state, new Region(regionId, regionName));
    }

}
