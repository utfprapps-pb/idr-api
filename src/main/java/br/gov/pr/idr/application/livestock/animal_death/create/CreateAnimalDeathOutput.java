package br.gov.pr.idr.application.livestock.animal_death.create;

import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDeathOutput(
        UUID id,
        UUID animalId,
        String reason,
        LocalDate deathDate
) {

    public static CreateAnimalDeathOutput from(final AnimalDeath entity) {
        return new CreateAnimalDeathOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getReason(),
                entity.getDeathDate()
        );
    }
}
