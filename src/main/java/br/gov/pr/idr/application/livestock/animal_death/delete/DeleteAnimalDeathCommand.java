package br.gov.pr.idr.application.livestock.animal_death.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record DeleteAnimalDeathCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDeathId,
        PropertySearchScope scope
) {
}
