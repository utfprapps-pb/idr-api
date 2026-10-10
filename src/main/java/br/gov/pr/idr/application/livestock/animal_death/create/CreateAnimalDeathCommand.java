package br.gov.pr.idr.application.livestock.animal_death.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDeathCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        String reason,
        LocalDate deathDate
) {
}
