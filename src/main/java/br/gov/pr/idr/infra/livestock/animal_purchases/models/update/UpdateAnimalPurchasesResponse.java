package br.gov.pr.idr.infra.livestock.animal_purchases.models.update;

import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesOutput;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalPurchasesResponse(
        UUID id,
        UUID animalId,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value
) {

    public static UpdateAnimalPurchasesResponse from(final UpdateAnimalPurchasesOutput output) {
        return new UpdateAnimalPurchasesResponse(
                output.id(),
                output.animalId(),
                output.datePurchase(),
                output.birthDate(),
                output.value()
        );
    }
}
