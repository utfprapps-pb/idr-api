package br.gov.pr.idr.application.livestock.animalsize.update;

import java.util.UUID;

public record UpdateAnimalSizeCommand(UUID id, String sizeName) {
}
