package br.gov.pr.idr.application.livestock.animal_purchases.update;

import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalPurchasesOutput(
        UUID id,
        UUID animalId,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value,
        BigDecimal weight
) {

    public static UpdateAnimalPurchasesOutput from(final AnimalPurchases entity) {
        return new UpdateAnimalPurchasesOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDatePurchase(),
                entity.getBirthDate(),
                entity.getValue(),
                entity.getWeight()
        );
    }
}
