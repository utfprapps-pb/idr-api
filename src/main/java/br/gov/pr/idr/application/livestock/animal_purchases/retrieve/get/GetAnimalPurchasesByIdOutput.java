package br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get;

import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GetAnimalPurchasesByIdOutput(
        UUID id,
        UUID animalId,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value
) {

    public static GetAnimalPurchasesByIdOutput from(final AnimalPurchases entity) {
        return new GetAnimalPurchasesByIdOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDatePurchase(),
                entity.getBirthDate(),
                entity.getValue()
        );
    }
}
