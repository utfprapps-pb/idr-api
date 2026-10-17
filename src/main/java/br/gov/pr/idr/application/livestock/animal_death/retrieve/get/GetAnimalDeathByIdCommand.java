package br.gov.pr.idr.application.livestock.animal_death.retrieve.get;

import java.util.UUID;

public record GetAnimalDeathByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDeathId,
        String username
) {
}
