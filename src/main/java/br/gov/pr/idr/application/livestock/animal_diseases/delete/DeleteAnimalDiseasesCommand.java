package br.gov.pr.idr.application.livestock.animal_diseases.delete;

import java.util.UUID;

public record DeleteAnimalDiseasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDiseasesId,
        String username
) {
}
