package br.gov.pr.idr.application.livestock.mastitis.retrieve.list;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListMastitisCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        SearchQuery query
) {
}
