package br.gov.pr.idr.infra.livestock.animalsize.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAnimalSizeRequest(
        @NotBlank(message = "Nome do porte não pode ser vazio")
        @Size(max = 255, message = "Nome do porte deve ter no máximo 255 caracteres") String sizeName) {
}
