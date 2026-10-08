package br.gov.pr.idr.application.livestock.animal.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalCommand(
        UUID propertyId,
        String username,
        UUID animalMotherId,
        UUID breedId,
        String type,
        String identifier,
        String gender,
        String bornCondition,
        LocalDate bornDate,
        Float bornWeight,
        Float previousWeight,
        Float currentWeight,
        Float ecc
) {
}
