package br.gov.pr.idr.application.livestock.animal_death.delete;

import java.util.UUID;

public record DeleteAnimalDeathCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDeathId,
        String username
) {
}
