package br.gov.pr.idr.infra.livestock.breed.models.update;

import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedOutput;

import java.util.UUID;

public record UpdateBreedResponse(UUID id, String breedName) {

    public static UpdateBreedResponse from(final UpdateBreedOutput output) {
        return new UpdateBreedResponse(output.id(), output.breedName());
    }
}
