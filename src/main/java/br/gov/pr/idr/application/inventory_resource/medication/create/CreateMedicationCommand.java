package br.gov.pr.idr.application.inventory_resource.medication.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.time.LocalDate;
import java.util.UUID;

public record CreateMedicationCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
