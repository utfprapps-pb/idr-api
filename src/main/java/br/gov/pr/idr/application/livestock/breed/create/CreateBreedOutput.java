package br.gov.pr.idr.application.livestock.breed.create;

import br.gov.pr.idr.domain.livestock.breed.Breed;

import java.util.UUID;

public record CreateBreedOutput(UUID id, String breedName) {

    public static CreateBreedOutput from(final Breed entity) {
        return new CreateBreedOutput(entity.getId().id(), entity.getBreedName());
    }
}
