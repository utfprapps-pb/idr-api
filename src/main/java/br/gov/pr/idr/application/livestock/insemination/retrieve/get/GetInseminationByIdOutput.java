package br.gov.pr.idr.application.livestock.insemination.retrieve.get;

import br.gov.pr.idr.domain.livestock.insemination.Insemination;

import java.time.LocalDate;
import java.util.UUID;

public record GetInseminationByIdOutput(
        UUID id,
        UUID animalId,
        LocalDate iaDate,
        String bull
) {

    public static GetInseminationByIdOutput from(final Insemination entity) {
        return new GetInseminationByIdOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getIaDate(),
                entity.getBull()
        );
    }
}
