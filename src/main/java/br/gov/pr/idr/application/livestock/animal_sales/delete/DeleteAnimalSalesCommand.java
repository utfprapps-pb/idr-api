package br.gov.pr.idr.application.livestock.animal_sales.delete;

import java.util.UUID;

public record DeleteAnimalSalesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalSalesId,
        String username
) {
}
