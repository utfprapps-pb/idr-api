package br.gov.pr.idr.infra.inventory_resource.product_category.models;

import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryOutput;

import java.util.UUID;

public record CreateProductCategoryResponse(UUID id, String description) {

    public static CreateProductCategoryResponse from(final CreateProductCategoryOutput output) {
        return new CreateProductCategoryResponse(output.id(), output.description());
    }
}
