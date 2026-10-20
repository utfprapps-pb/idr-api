package br.gov.pr.idr.infra.livestock.animal_death.models;

import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDeathResponse(
        UUID id,
        UUID animalId,
        String reason,
        LocalDate deathDate
) {

    public static CreateAnimalDeathResponse from(final CreateAnimalDeathOutput output) {
        return new CreateAnimalDeathResponse(
                output.id(),
                output.animalId(),
                output.reason(),
                output.deathDate()
        );
    }
}
