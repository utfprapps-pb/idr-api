package br.gov.pr.idr.infra.livestock.animal_death.models.update;

import java.time.LocalDate;

public record UpdateAnimalDeathRequest(
        String reason,
        LocalDate deathDate
) {
}
