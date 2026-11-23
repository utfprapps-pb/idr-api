package br.gov.pr.idr.application.livestock.animal.retrieve.get;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;

import java.time.LocalDate;
import java.util.UUID;

public record GetAnimalByIdOutput(
        UUID id,
        UUID propertyId,
        UUID animalMotherId,
        String animalMotherIdentifier,
        UUID breedId,
        String breedName,
        UUID sizeId,
        String sizeName,
        String identifier,
        String gender,
        String bornCondition,
        LocalDate bornDate,
        Float bornWeight,
        Float previousWeight,
        Float currentWeight,
        Float ecc
) {

    public static GetAnimalByIdOutput from(final Animal entity, final String breedName, final String sizeName,
                                    final String animalMotherIdentifier) {
        return new GetAnimalByIdOutput(
                entity.getId().id(),
                entity.getPropertyId().id(),
                entity.getAnimalMotherId().map(id -> id.id()).orElse(null),
                animalMotherIdentifier,
                entity.getBreedId().id(),
                breedName,
                entity.getSizeId().map(AnimalSizeID::id).orElse(null),
                sizeName,
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
