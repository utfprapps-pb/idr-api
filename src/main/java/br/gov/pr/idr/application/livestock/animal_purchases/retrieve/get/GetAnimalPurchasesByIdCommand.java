package br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get;

import java.util.UUID;

public record GetAnimalPurchasesByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalPurchasesId,
        String username
) {
}
