package br.gov.pr.idr.application.livestock.insemination.retrieve.list;

import br.gov.pr.idr.domain.livestock.insemination.Insemination;

import java.time.LocalDate;
import java.util.UUID;

public record ListInseminationOutput(
        UUID id,
        UUID animalId,
        LocalDate iaDate,
        String bull
) {

    public static ListInseminationOutput from(final Insemination entity) {
        return new ListInseminationOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getIaDate(),
                entity.getBull()
        );
    }
}
