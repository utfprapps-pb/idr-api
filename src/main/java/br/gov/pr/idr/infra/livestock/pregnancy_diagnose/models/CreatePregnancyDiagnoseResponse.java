package br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models;

import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePregnancyDiagnoseResponse(
        UUID id,
        UUID animalId,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {

    public static CreatePregnancyDiagnoseResponse from(final CreatePregnancyDiagnoseOutput output) {
        return new CreatePregnancyDiagnoseResponse(
                output.id(),
                output.animalId(),
                output.diagnoseDate(),
                output.lastIa()
        );
    }
}
