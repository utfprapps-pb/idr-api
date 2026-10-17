package br.gov.pr.idr.infra.livestock.breed.models;

import jakarta.validation.constraints.NotBlank;

public record CreateBreedRequest(
        @NotBlank(message = "Nome da raça não pode ser vazio") String breedName) {
}
