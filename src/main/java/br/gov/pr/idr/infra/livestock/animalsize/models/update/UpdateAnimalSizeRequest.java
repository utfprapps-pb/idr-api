package br.gov.pr.idr.infra.livestock.animalsize.models.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateAnimalSizeRequest(
        @NotBlank(message = "Nome do porte não pode ser vazio") String sizeName) {
}
