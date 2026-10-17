package br.gov.pr.idr.application.livestock.pregnancy_diagnose.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {
}
