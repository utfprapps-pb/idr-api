package br.gov.pr.idr.infra.livestock.animal.models.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalRequest(
        UUID animalMotherId,
        @NotNull(message = "A raça do animal é obrigatória") UUID breedId,
        String type,
        @NotBlank(message = "O identificador do animal é obrigatório") String identifier,
        String gender,
        String bornCondition,
        LocalDate bornDate,
        Float bornWeight,
        Float previousWeight,
        Float currentWeight,
        Float ecc
) {
}
