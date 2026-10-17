package br.gov.pr.idr.application.livestock.insemination.update;

import br.gov.pr.idr.domain.livestock.insemination.Insemination;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateInseminationOutput(
        UUID id,
        UUID animalId,
        LocalDate iaDate,
        String bull
) {

    public static UpdateInseminationOutput from(final Insemination entity) {
        return new UpdateInseminationOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getIaDate(),
                entity.getBull()
        );
    }
}
