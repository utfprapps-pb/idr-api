package br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models.update;

import java.time.LocalDate;

public record UpdatePregnancyDiagnoseRequest(
        LocalDate diagnoseDate,
        LocalDate lastIa
) {
}
