package br.gov.pr.idr.infra.inventory_resource.active_principle.models.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateActivePrincipleRequest(
        @NotBlank(message = "Nome do princípio ativo não pode ser vazio") String name) {
}
