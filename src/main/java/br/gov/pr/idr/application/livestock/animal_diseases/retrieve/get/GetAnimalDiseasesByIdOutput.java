package br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get;

import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;

import java.time.LocalDate;
import java.util.UUID;

public record GetAnimalDiseasesByIdOutput(
        UUID id,
        UUID animalId,
        String diagnosis,
        LocalDate diagnosisDate
) {

    public static GetAnimalDiseasesByIdOutput from(final AnimalDiseases entity) {
        return new GetAnimalDiseasesByIdOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnosis(),
                entity.getDiagnosisDate()
        );
    }
}
