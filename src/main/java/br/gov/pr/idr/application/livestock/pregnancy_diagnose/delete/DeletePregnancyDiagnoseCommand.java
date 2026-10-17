package br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete;

import java.util.UUID;

public record DeletePregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        UUID pregnancyDiagnoseId,
        String username
) {
}
