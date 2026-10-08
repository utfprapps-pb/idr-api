package br.gov.pr.idr.application.livestock.breed.retrieve.list;

import br.gov.pr.idr.domain.livestock.breed.Breed;

import java.util.UUID;

public record ListBreedOutput(UUID id, String breedName) {

    public static ListBreedOutput from(final Breed entity) {
        return new ListBreedOutput(entity.getId().id(), entity.getBreedName());
    }
}
