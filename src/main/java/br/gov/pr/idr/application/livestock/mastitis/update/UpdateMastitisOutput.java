package br.gov.pr.idr.application.livestock.mastitis.update;

import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMastitisOutput(
        UUID id,
        UUID animalId,
        LocalDate diagnoseDate,
        String mastitisType,
        String cmtResult,
        String ad,
        String ae,
        String pd,
        String pe
) {

    public static UpdateMastitisOutput from(final Mastitis entity) {
        return new UpdateMastitisOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDiagnoseDate(),
                entity.getMastitisType(),
                entity.getCmtResult(),
                entity.getAd(),
                entity.getAe(),
                entity.getPd(),
                entity.getPe()
        );
    }
}
