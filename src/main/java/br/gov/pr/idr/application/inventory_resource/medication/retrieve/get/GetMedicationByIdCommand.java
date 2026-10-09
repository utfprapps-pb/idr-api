package br.gov.pr.idr.application.inventory_resource.medication.retrieve.get;

import java.util.UUID;

public record GetMedicationByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID medicationId,
        String username
) {
}
