package br.gov.pr.idr.infra.livestock.animal_purchases.models;

import br.gov.pr.idr.application.livestock.animal_purchases.create.CreateAnimalPurchasesOutput;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalPurchasesResponse(
        UUID id,
        UUID animalId,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value
) {

    public static CreateAnimalPurchasesResponse from(final CreateAnimalPurchasesOutput output) {
        return new CreateAnimalPurchasesResponse(
                output.id(),
                output.animalId(),
                output.datePurchase(),
                output.birthDate(),
                output.value()
        );
    }
}
