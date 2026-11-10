package br.gov.pr.idr.infra.inventory_resource.medication.models.update;

import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMedicationResponse(
        UUID id,
        UUID animalId,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {

    public static UpdateMedicationResponse from(final UpdateMedicationOutput output) {
        return new UpdateMedicationResponse(
                output.id(),
                output.animalId(),
                output.productId(),
                output.applicationDate(),
                output.appliedDose(),
                output.applicationWay()
        );
    }
}
