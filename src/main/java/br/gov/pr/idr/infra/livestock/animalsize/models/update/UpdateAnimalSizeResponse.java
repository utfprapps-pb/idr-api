package br.gov.pr.idr.infra.livestock.animalsize.models.update;

import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeOutput;

import java.util.UUID;

public record UpdateAnimalSizeResponse(UUID id, String sizeName) {

    public static UpdateAnimalSizeResponse from(final UpdateAnimalSizeOutput output) {
        return new UpdateAnimalSizeResponse(output.id(), output.sizeName());
    }
}
