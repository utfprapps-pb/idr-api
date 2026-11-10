package br.gov.pr.idr.application.inventory_resource.medication.retrieve.list;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;

import java.time.LocalDate;
import java.util.UUID;

public record ListMedicationOutput(
        UUID id,
        UUID animalId,
        UUID productId,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {

    public static ListMedicationOutput from(final Medication entity) {
        return new ListMedicationOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getProductId().id(),
                entity.getApplicationDate(),
                entity.getAppliedDose(),
                entity.getApplicationWay()
        );
    }
}
