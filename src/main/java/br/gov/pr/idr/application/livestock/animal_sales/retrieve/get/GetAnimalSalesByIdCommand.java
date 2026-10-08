package br.gov.pr.idr.application.livestock.animal_sales.retrieve.get;

import java.util.UUID;

public record GetAnimalSalesByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalSalesId,
        String username
) {
}
