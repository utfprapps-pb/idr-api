package br.gov.pr.idr.application.inventory_resource.medication.create;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMedicationOutput(
        UUID id,
        UUID animalId,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {

    public static CreateMedicationOutput from(final Medication entity) {
        return new CreateMedicationOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getProductId().id(),
                entity.getApplicationDate(),
                entity.getAppliedDose(),
                entity.getApplicationWay()
        );
    }
}
