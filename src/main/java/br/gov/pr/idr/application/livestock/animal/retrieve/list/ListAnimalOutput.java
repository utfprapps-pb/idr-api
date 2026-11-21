package br.gov.pr.idr.application.livestock.animal.retrieve.list;

import br.gov.pr.idr.domain.livestock.animal.Animal;

import java.time.LocalDate;
import java.util.UUID;

public record ListAnimalOutput(
        UUID id,
        UUID propertyId,
        UUID animalMotherId,
        String animalMotherIdentifier,
        UUID breedId,
        String breedName,
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

    public static ListAnimalOutput from(final Animal entity, final String breedName,
                                    final String animalMotherIdentifier) {
        return new ListAnimalOutput(
                entity.getId().id(),
                entity.getPropertyId().id(),
                entity.getAnimalMotherId().map(id -> id.id()).orElse(null),
                animalMotherIdentifier,
                entity.getBreedId().id(),
                breedName,
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
