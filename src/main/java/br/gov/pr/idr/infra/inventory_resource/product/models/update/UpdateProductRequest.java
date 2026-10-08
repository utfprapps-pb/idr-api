package br.gov.pr.idr.infra.inventory_resource.product.models.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateProductRequest(
        @NotBlank(message = "Nome não pode ser nulo") String name,
        @NotBlank(message = "Descrição não pode ser nula") String description,
        @NotBlank(message = "Via de aplicação não pode ser nula") String applicationWay,
        @NotNull(message = "Princípio ativo não pode ser nulo") UUID activePrincipleId,
        @NotNull(message = "Categoria não pode ser nula") UUID categoryId
) {
}
