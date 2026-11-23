package br.gov.pr.idr.application.livestock.animalsize.create;

import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;

import java.util.UUID;

public record CreateAnimalSizeOutput(UUID id, String sizeName) {

    public static CreateAnimalSizeOutput from(final AnimalSize entity) {
        return new CreateAnimalSizeOutput(entity.getId().id(), entity.getSizeName());
    }
}
