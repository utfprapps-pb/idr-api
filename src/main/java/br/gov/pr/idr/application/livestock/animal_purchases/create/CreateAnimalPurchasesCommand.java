package br.gov.pr.idr.application.livestock.animal_purchases.create;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalPurchasesCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value,
        BigDecimal weight
) {
}
