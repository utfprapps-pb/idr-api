package br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListAnimalPurchasesCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        SearchQuery query
) {
}
