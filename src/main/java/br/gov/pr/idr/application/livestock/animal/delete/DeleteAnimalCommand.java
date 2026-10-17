package br.gov.pr.idr.application.livestock.animal.delete;

import java.util.UUID;

public record DeleteAnimalCommand(
        UUID propertyId,
        UUID animalId,
        String username
) {
}
