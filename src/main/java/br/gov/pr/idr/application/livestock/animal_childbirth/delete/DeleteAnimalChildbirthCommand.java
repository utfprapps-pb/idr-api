package br.gov.pr.idr.application.livestock.animal_childbirth.delete;

import java.util.UUID;

public record DeleteAnimalChildbirthCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalChildbirthId,
        String username
) {
}
