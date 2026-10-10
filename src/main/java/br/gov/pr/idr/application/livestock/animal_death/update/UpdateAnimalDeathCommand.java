package br.gov.pr.idr.application.livestock.animal_death.update;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDeathCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDeathId,
        String username,
        String reason,
        LocalDate deathDate
) {
}
