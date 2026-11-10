package br.gov.pr.idr.infra.livestock.mastitis.models.update;

import br.gov.pr.idr.application.livestock.mastitis.update.UpdateMastitisOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMastitisResponse(
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

    public static UpdateMastitisResponse from(final UpdateMastitisOutput output) {
        return new UpdateMastitisResponse(
                output.id(),
                output.animalId(),
                output.diagnoseDate(),
                output.mastitisType(),
                output.cmtResult(),
                output.ad(),
                output.ae(),
                output.pd(),
                output.pe()
        );
    }
}
