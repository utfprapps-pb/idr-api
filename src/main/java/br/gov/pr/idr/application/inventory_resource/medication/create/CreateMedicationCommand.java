package br.gov.pr.idr.application.inventory_resource.medication.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMedicationCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
