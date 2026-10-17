package br.gov.pr.idr.infra.livestock.insemination.models;

import br.gov.pr.idr.application.livestock.insemination.create.CreateInseminationOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreateInseminationResponse(
        UUID id,
        UUID animalId,
        LocalDate iaDate,
        String bull
) {

    public static CreateInseminationResponse from(final CreateInseminationOutput output) {
        return new CreateInseminationResponse(
                output.id(),
                output.animalId(),
                output.iaDate(),
                output.bull()
        );
    }
}
