package br.gov.pr.idr.application.livestock.pregnancy_diagnose.update;

import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePregnancyDiagnoseOutput(
        UUID id,
        UUID animalId,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {

    public static UpdatePregnancyDiagnoseOutput from(final PregnancyDiagnose entity) {
        return new UpdatePregnancyDiagnoseOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnoseDate(),
                entity.getLastIa()
        );
    }
}
