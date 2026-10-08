package br.gov.pr.idr.application.livestock.animal_purchases.update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalPurchasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalPurchasesId,
        String username,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value
) {
}
