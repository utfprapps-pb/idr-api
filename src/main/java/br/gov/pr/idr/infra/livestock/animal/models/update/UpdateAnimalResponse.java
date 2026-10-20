package br.gov.pr.idr.infra.livestock.animal.models.update;

import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalOutput;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalResponse(
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

    public static UpdateAnimalResponse from(final UpdateAnimalOutput output) {
        return new UpdateAnimalResponse(
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
