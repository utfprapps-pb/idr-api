package br.gov.pr.idr.application.livestock.animal_purchases.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record DeleteAnimalPurchasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalPurchasesId,
        PropertySearchScope scope
) {
}
