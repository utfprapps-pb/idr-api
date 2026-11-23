package br.gov.pr.idr.application.livestock.animalsize.retrieve.list;

import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;

import java.util.UUID;

public record ListAnimalSizeOutput(UUID id, String sizeName) {

    public static ListAnimalSizeOutput from(final AnimalSize entity) {
        return new ListAnimalSizeOutput(entity.getId().id(), entity.getSizeName());
    }
}
