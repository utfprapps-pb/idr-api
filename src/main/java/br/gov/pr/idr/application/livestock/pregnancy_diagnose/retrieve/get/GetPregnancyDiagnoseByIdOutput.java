package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get;

import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;

import java.time.LocalDate;
import java.util.UUID;

public record GetPregnancyDiagnoseByIdOutput(
        UUID id,
        UUID animalId,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {

    public static GetPregnancyDiagnoseByIdOutput from(final PregnancyDiagnose entity) {
        return new GetPregnancyDiagnoseByIdOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnoseDate(),
                entity.getLastIa()
        );
    }
}
