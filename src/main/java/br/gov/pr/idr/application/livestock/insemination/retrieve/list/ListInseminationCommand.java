package br.gov.pr.idr.application.livestock.insemination.retrieve.list;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListInseminationCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        SearchQuery query
) {
}
