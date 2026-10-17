package br.gov.pr.idr.application.inventory_resource.medication.update;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMedicationCommand(
        UUID propertyId,
        UUID animalId,
        UUID medicationId,
        String username,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
