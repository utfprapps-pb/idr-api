package br.gov.pr.idr.application.livestock.animal_purchases.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalPurchasesCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        LocalDate datePurchase,
        LocalDate birthDate,
        BigDecimal value,
        BigDecimal weight
) {
}
