package br.gov.pr.idr.application.livestock.animal.retrieve.list;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListAnimalCommand(
        UUID propertyId,
        PropertySearchScope scope,
        SearchQuery query
) {
}
