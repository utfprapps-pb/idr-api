package br.gov.pr.idr.infra.livestock.animal_death.models;

import java.time.LocalDate;

public record CreateAnimalDeathRequest(
        String reason,
        LocalDate deathDate
) {
}
