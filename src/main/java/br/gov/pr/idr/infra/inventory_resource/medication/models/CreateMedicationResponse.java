package br.gov.pr.idr.infra.inventory_resource.medication.models;

import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMedicationResponse(
        UUID id,
        UUID animalId,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {

    public static CreateMedicationResponse from(final CreateMedicationOutput output) {
        return new CreateMedicationResponse(
                output.id(),
                output.animalId(),
                output.productId(),
                output.applicationDate(),
                output.appliedDose(),
                output.applicationWay()
        );
    }
}
