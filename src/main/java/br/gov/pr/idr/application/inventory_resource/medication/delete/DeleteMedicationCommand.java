package br.gov.pr.idr.application.inventory_resource.medication.delete;

import java.util.UUID;

public record DeleteMedicationCommand(
        UUID propertyId,
        UUID animalId,
        UUID medicationId,
        String username
) {
}
