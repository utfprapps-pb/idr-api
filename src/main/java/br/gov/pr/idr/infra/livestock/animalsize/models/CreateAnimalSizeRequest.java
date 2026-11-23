package br.gov.pr.idr.infra.livestock.animalsize.models;

import jakarta.validation.constraints.NotBlank;

public record CreateAnimalSizeRequest(
        @NotBlank(message = "Nome do porte não pode ser vazio") String sizeName) {
}
