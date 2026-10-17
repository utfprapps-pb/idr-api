package br.gov.pr.idr.application.livestock.pregnancy_diagnose.update;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        UUID pregnancyDiagnoseId,
        String username,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {
}
