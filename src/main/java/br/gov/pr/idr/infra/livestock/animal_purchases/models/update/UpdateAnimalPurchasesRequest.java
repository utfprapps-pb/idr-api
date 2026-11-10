package br.gov.pr.idr.infra.livestock.animal_purchases.models.update;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateAnimalPurchasesRequest(
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value,
        BigDecimal weight
) {
}
