package br.gov.pr.idr.application.livestock.animal_sales.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record DeleteAnimalSalesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalSalesId,
        PropertySearchScope scope
) {
}
