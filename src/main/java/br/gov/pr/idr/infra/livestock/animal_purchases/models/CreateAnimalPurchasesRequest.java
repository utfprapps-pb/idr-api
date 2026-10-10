package br.gov.pr.idr.infra.livestock.animal_purchases.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateAnimalPurchasesRequest(
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value,
        BigDecimal weight
) {
}
