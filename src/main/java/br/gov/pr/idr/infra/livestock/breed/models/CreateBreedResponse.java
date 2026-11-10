package br.gov.pr.idr.infra.livestock.breed.models;

import br.gov.pr.idr.application.livestock.breed.create.CreateBreedOutput;

import java.util.UUID;

public record CreateBreedResponse(UUID id, String breedName) {

    public static CreateBreedResponse from(final CreateBreedOutput output) {
        return new CreateBreedResponse(output.id(), output.breedName());
    }
}
