package br.gov.pr.idr.application.inventory_resource.medication.retrieve.list;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.UUID;

public record ListMedicationCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        SearchQuery query
) {
}
