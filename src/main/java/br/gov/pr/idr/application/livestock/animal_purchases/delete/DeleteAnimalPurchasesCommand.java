package br.gov.pr.idr.application.livestock.animal_purchases.delete;

import java.util.UUID;

public record DeleteAnimalPurchasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalPurchasesId,
        String username
) {
}
