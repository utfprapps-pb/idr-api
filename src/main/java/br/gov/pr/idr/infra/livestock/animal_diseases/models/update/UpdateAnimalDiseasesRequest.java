package br.gov.pr.idr.infra.livestock.animal_diseases.models.update;

import java.time.LocalDate;

public record UpdateAnimalDiseasesRequest(
        String diagnosis,
        LocalDate diagnosisDate
) {
}
