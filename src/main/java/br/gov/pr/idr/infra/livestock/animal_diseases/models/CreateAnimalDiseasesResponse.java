package br.gov.pr.idr.infra.livestock.animal_diseases.models;

import br.gov.pr.idr.application.livestock.animal_diseases.create.CreateAnimalDiseasesOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDiseasesResponse(
        UUID id,
        UUID animalId,
        String diagnosis,
        LocalDate diagnosisDate
) {

    public static CreateAnimalDiseasesResponse from(final CreateAnimalDiseasesOutput output) {
        return new CreateAnimalDiseasesResponse(
                output.id(),
                output.animalId(),
                output.diagnosis(),
                output.diagnosisDate()
        );
    }
}
