package br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record GetAnimalPurchasesByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalPurchasesId,
        PropertySearchScope scope
) {
}
