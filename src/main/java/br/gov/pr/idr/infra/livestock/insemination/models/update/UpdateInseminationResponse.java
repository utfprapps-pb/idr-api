package br.gov.pr.idr.infra.livestock.insemination.models.update;

import br.gov.pr.idr.application.livestock.insemination.update.UpdateInseminationOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateInseminationResponse(
        UUID id,
        UUID animalId,
        LocalDate iaDate,
        String bull
) {

    public static UpdateInseminationResponse from(final UpdateInseminationOutput output) {
        return new UpdateInseminationResponse(
                output.id(),
                output.animalId(),
                output.iaDate(),
                output.bull()
        );
    }
}
