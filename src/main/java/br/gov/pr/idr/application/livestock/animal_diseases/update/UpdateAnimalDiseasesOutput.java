package br.gov.pr.idr.application.livestock.animal_diseases.update;

import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDiseasesOutput(
        UUID id,
        UUID animalId,
        String diagnosis,
        LocalDate diagnosisDate
) {

    public static UpdateAnimalDiseasesOutput from(final AnimalDiseases entity) {
        return new UpdateAnimalDiseasesOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnosis(),
                entity.getDiagnosisDate()
        );
    }
}
