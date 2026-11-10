package br.gov.pr.idr.infra.livestock.pregnancy_diagnose.models.update;

import br.gov.pr.idr.application.livestock.pregnancy_diagnose.update.UpdatePregnancyDiagnoseOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePregnancyDiagnoseResponse(
        UUID id,
        UUID animalId,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {

    public static UpdatePregnancyDiagnoseResponse from(final UpdatePregnancyDiagnoseOutput output) {
        return new UpdatePregnancyDiagnoseResponse(
                output.id(),
                output.animalId(),
                output.diagnoseDate(),
                output.lastIa()
        );
    }
}
