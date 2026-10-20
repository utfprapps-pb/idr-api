package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list;

import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;

import java.time.LocalDate;
import java.util.UUID;

public record ListPregnancyDiagnoseOutput(
        UUID id,
        UUID animalId,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {

    public static ListPregnancyDiagnoseOutput from(final PregnancyDiagnose entity) {
        return new ListPregnancyDiagnoseOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnoseDate(),
                entity.getLastIa()
        );
    }
}
