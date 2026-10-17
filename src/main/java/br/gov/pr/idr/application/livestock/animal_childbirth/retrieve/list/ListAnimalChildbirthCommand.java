package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListAnimalChildbirthCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        SearchQuery query
) {
}
