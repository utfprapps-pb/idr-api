package br.gov.pr.idr.application.inventory_resource.product_category.create;

import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;

import java.util.UUID;

public record CreateProductCategoryOutput(UUID id, String description) {

    public static CreateProductCategoryOutput from(final ProductCategory entity) {
        return new CreateProductCategoryOutput(entity.getId().id(), entity.getDescription());
    }
}
