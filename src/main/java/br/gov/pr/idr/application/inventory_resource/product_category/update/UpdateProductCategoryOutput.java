package br.gov.pr.idr.application.inventory_resource.product_category.update;

import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;

import java.util.UUID;

public record UpdateProductCategoryOutput(UUID id, String description) {

    public static UpdateProductCategoryOutput from(final ProductCategory entity) {
        return new UpdateProductCategoryOutput(entity.getId().id(), entity.getDescription());
    }
}
