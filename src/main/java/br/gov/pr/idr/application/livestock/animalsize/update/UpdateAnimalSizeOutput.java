package br.gov.pr.idr.application.livestock.animalsize.update;

import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;

import java.util.UUID;

public record UpdateAnimalSizeOutput(UUID id, String sizeName) {

    public static UpdateAnimalSizeOutput from(final AnimalSize entity) {
        return new UpdateAnimalSizeOutput(entity.getId().id(), entity.getSizeName());
    }
}
