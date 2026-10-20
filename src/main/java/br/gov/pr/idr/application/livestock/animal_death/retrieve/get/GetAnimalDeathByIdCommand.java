package br.gov.pr.idr.application.livestock.animal_death.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record GetAnimalDeathByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDeathId,
        PropertySearchScope scope
) {
}
