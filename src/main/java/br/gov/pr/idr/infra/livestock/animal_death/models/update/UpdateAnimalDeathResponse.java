package br.gov.pr.idr.infra.livestock.animal_death.models.update;

import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDeathResponse(
        UUID id,
        UUID animalId,
        String reason,
        LocalDate deathDate
) {

    public static UpdateAnimalDeathResponse from(final UpdateAnimalDeathOutput output) {
        return new UpdateAnimalDeathResponse(
                output.id(),
                output.animalId(),
                output.reason(),
                output.deathDate()
        );
    }
}
