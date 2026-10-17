package br.gov.pr.idr.application.livestock.animal_diseases.update;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDiseasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDiseasesId,
        String username,
        String diagnosis,
        LocalDate diagnosisDate
) {
}
