package br.gov.pr.idr.infra.livestock.breed.models.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateBreedRequest(
        @NotBlank(message = "Nome da raça não pode ser vazio") String breedName) {
}
