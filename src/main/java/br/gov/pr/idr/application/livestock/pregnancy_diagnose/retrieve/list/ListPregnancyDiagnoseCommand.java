package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListPregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        SearchQuery query
) {
}
