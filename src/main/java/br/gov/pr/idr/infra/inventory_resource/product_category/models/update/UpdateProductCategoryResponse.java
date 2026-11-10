package br.gov.pr.idr.infra.inventory_resource.product_category.models.update;

import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryOutput;

import java.util.UUID;

public record UpdateProductCategoryResponse(UUID id, String description) {

    public static UpdateProductCategoryResponse from(final UpdateProductCategoryOutput output) {
        return new UpdateProductCategoryResponse(output.id(), output.description());
    }
}
