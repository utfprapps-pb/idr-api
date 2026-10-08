package br.gov.pr.idr.infra.inventory_resource.product_category.models;

import jakarta.validation.constraints.NotBlank;

public record CreateProductCategoryRequest(
        @NotBlank(message = "Descrição da categoria de produto não pode ser vazia") String description) {
}
