package br.gov.pr.idr.infra.inventory_resource.active_principle.models;

import jakarta.validation.constraints.NotBlank;

public record CreateActivePrincipleRequest(
        @NotBlank(message = "Nome do princípio ativo não pode ser vazio") String name) {
}
