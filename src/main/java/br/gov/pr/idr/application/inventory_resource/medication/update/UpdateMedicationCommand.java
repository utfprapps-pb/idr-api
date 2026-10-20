package br.gov.pr.idr.application.inventory_resource.medication.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMedicationCommand(
        UUID propertyId,
        UUID animalId,
        UUID medicationId,
        PropertySearchScope scope,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
