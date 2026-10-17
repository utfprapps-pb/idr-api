package br.gov.pr.idr.application.livestock.animal_diseases.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDiseasesCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        String diagnosis,
        LocalDate diagnosisDate
) {
}
