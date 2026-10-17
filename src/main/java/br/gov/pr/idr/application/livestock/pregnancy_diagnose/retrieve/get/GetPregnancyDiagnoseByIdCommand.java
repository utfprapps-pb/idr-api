package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get;

import java.util.UUID;

public record GetPregnancyDiagnoseByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID pregnancyDiagnoseId,
        String username
) {
}
