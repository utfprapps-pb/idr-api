package br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list;

import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;

import java.time.LocalDate;
import java.util.UUID;

public record ListAnimalDiseasesOutput(
        UUID id,
        UUID animalId,
        String diagnosis,
        LocalDate diagnosisDate
) {

    public static ListAnimalDiseasesOutput from(final AnimalDiseases entity) {
        return new ListAnimalDiseasesOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnosis(),
                entity.getDiagnosisDate()
        );
    }
}
