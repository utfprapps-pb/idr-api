package br.gov.pr.idr.infra.property_management.region.models;

import jakarta.validation.constraints.NotBlank;

public record CreateRegionRequest(@NotBlank(message = "Descrição da região não pode ser vazio") String description) {
}
