package br.gov.pr.idr.infra.livestock.animal_diseases.models.update;

import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDiseasesResponse(
        UUID id,
        UUID animalId,
        String diagnosis,
        LocalDate diagnosisDate
) {

    public static UpdateAnimalDiseasesResponse from(final UpdateAnimalDiseasesOutput output) {
        return new UpdateAnimalDiseasesResponse(
                output.id(),
                output.animalId(),
                output.diagnosis(),
                output.diagnosisDate()
        );
    }
}
