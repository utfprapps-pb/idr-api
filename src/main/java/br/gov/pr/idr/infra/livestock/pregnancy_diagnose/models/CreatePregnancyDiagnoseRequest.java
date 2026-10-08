package br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models;

import java.time.LocalDate;

public record CreatePregnancyDiagnoseRequest(
        LocalDate diagnoseDate,
        LocalDate lastIa
) {
}
