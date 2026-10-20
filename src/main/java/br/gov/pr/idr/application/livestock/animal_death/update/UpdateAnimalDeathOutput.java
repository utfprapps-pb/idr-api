package br.gov.pr.idr.application.livestock.animal_death.update;

import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDeathOutput(
        UUID id,
        UUID animalId,
        String reason,
        LocalDate deathDate
) {

    public static UpdateAnimalDeathOutput from(final AnimalDeath entity) {
        return new UpdateAnimalDeathOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getReason(),
                entity.getDeathDate()
        );
    }
}
