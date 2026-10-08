package br.gov.pr.idr.application.livestock.breed.update;

import java.util.UUID;

public record UpdateBreedCommand(UUID id, String breedName) {
}
