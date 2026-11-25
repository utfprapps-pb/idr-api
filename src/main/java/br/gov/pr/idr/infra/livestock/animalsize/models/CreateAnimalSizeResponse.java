package br.gov.pr.idr.infra.livestock.animalsize.models;

import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeOutput;

import java.util.UUID;

public record CreateAnimalSizeResponse(UUID id, String sizeName) {

    public static CreateAnimalSizeResponse from(final CreateAnimalSizeOutput output) {
        return new CreateAnimalSizeResponse(output.id(), output.sizeName());
    }
}
