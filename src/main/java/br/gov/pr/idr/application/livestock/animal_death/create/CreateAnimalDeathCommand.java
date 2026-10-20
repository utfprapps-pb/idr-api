package br.gov.pr.idr.application.livestock.animal_death.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDeathCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        String reason,
        LocalDate deathDate
) {
}
