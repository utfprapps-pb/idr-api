package br.gov.pr.idr.application.livestock.animal.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record DeleteAnimalCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope
) {
}
