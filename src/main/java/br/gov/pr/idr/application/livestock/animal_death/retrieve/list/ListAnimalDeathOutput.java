package br.gov.pr.idr.application.livestock.animal_death.retrieve.list;

import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;

import java.time.LocalDate;
import java.util.UUID;

public record ListAnimalDeathOutput(
        UUID id,
        UUID animalId,
        String reason,
        LocalDate deathDate
) {

    public static ListAnimalDeathOutput from(final AnimalDeath entity) {
        return new ListAnimalDeathOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getReason(),
                entity.getDeathDate()
        );
    }
}
