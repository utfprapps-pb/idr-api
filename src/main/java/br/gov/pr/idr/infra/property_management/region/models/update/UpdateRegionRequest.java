package br.gov.pr.idr.infra.property_management.region.models.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateRegionRequest(@NotBlank(message = "Descrição da região não pode ser vazio") String description) {
}
