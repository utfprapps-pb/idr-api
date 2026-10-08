package br.gov.pr.idr.application.livestock.animal_sales.retrieve.list;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListAnimalSalesCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        SearchQuery query
) {
}
