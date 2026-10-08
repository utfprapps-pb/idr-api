package br.gov.pr.idr.infra.inventory_resource.product_category.models.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateProductCategoryRequest(
        @NotBlank(message = "Descrição da categoria de produto não pode ser vazio") String description) {
}
