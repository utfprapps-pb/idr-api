package br.gov.pr.idr.application.livestock.animal.retrieve.get;

import java.util.UUID;

public record GetAnimalByIdCommand(
        UUID propertyId,
        UUID animalId,
        String username
) {
}
