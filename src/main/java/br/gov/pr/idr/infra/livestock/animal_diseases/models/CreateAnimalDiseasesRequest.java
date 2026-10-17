package br.gov.pr.idr.infra.livestock.animal_diseases.models;

import java.time.LocalDate;

public record CreateAnimalDiseasesRequest(
        String diagnosis,
        LocalDate diagnosisDate
) {
}
