package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get;

import java.util.UUID;

public record GetAnimalChildbirthByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalChildbirthId,
        String username
) {
}
