package br.gov.pr.idr.application.livestock.breed.update;

import br.gov.pr.idr.domain.livestock.breed.Breed;

import java.util.UUID;

public record UpdateBreedOutput(UUID id, String breedName) {

    public static UpdateBreedOutput from(final Breed entity) {
        return new UpdateBreedOutput(entity.getId().id(), entity.getBreedName());
    }
}
