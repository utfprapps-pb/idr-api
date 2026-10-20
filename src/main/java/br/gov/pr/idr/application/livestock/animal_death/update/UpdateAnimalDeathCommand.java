package br.gov.pr.idr.application.livestock.animal_death.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDeathCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDeathId,
        PropertySearchScope scope,
        String reason,
        LocalDate deathDate
) {
}
