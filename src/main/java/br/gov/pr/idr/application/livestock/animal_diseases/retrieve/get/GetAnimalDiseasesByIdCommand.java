package br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get;

import java.util.UUID;

public record GetAnimalDiseasesByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDiseasesId,
        String username
) {
}
