package br.gov.pr.idr.application.inventory_resource.medication.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record DeleteMedicationCommand(
        UUID propertyId,
        UUID animalId,
        UUID medicationId,
        PropertySearchScope scope
) {
}
