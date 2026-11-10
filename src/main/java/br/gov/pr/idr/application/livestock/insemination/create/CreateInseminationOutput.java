package br.gov.pr.idr.application.livestock.insemination.create;

import br.gov.pr.idr.domain.livestock.insemination.Insemination;

import java.time.LocalDate;
import java.util.UUID;

public record CreateInseminationOutput(
        UUID id,
        UUID animalId,
        LocalDate iaDate,
        String bull
) {

    public static CreateInseminationOutput from(final Insemination entity) {
        return new CreateInseminationOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getIaDate(),
                entity.getBull()
        );
    }
}
