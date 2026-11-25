package br.gov.pr.idr.application.inventory_resource.medication.retrieve.get;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;

import java.time.LocalDate;
import java.util.UUID;

public record GetMedicationByIdOutput(
        UUID id,
        UUID animalId,
        UUID productId,
        String productName,
        String activePrinciple,
        LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {

    public static GetMedicationByIdOutput from(final Medication entity) {
        return new GetMedicationByIdOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getProductId() == null ? null : entity.getProductId().id(),
                entity.getProductName(),
                entity.getActivePrinciple(),
                entity.getApplicationDate(),
                entity.getAppliedDose(),
                entity.getApplicationWay()
        );
    }
}
