package br.gov.pr.idr.application.livestock.animal_death.retrieve.get;

import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;

import java.time.LocalDate;
import java.util.UUID;

public record GetAnimalDeathByIdOutput(
        UUID id,
        UUID animalId,
        String reason,
        LocalDate deathDate
) {

    public static GetAnimalDeathByIdOutput from(final AnimalDeath entity) {
        return new GetAnimalDeathByIdOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getReason(),
                entity.getDeathDate()
        );
    }
}
