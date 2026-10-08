package br.gov.pr.idr.application.livestock.mastitis.retrieve.list;

import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;

import java.time.LocalDate;
import java.util.UUID;

public record ListMastitisOutput(
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

    public static ListMastitisOutput from(final Mastitis entity) {
        return new ListMastitisOutput(
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
