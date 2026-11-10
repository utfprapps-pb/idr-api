package br.gov.pr.idr.application.livestock.animal_sales.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record GetAnimalSalesByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalSalesId,
        PropertySearchScope scope
) {
}
