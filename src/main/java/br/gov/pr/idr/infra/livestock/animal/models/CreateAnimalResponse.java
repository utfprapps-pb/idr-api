package br.gov.pr.idr.infra.livestock.animal.models;

import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalOutput;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalResponse(
        UUID id,
        UUID propertyId,
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

    public static CreateAnimalResponse from(final CreateAnimalOutput output) {
        return new CreateAnimalResponse(
                output.id(),
                output.propertyId(),
                output.animalMotherId(),
                output.breedId(),
                output.type(),
                output.identifier(),
                output.gender(),
                output.bornCondition(),
                output.bornDate(),
                output.bornWeight(),
                output.previousWeight(),
                output.currentWeight(),
                output.ecc()
        );
    }
}
