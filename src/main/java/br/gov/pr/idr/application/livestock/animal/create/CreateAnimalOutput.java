package br.gov.pr.idr.application.livestock.animal.create;

import br.gov.pr.idr.domain.livestock.animal.Animal;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalOutput(
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

    public static CreateAnimalOutput from(final Animal entity) {
        return new CreateAnimalOutput(
                entity.getId().id(),
                entity.getPropertyId().id(),
                entity.getAnimalMotherId().map(id -> id.id()).orElse(null),
                entity.getBreedId().id(),
                entity.getType(),
                entity.getIdentifier(),
                entity.getGender(),
                entity.getBornCondition(),
                entity.getBornDate(),
                entity.getBornWeight(),
                entity.getPreviousWeight(),
                entity.getCurrentWeight(),
                entity.getEcc()
        );
    }
}
