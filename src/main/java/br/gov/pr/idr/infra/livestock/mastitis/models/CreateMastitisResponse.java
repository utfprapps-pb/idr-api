package br.gov.pr.idr.infra.livestock.mastitis.models;

import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMastitisResponse(
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

    public static CreateMastitisResponse from(final CreateMastitisOutput output) {
        return new CreateMastitisResponse(
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
