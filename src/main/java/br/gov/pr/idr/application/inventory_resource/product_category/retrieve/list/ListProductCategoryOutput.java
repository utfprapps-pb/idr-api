package br.gov.pr.idr.application.inventory_resource.product_category.retrieve.list;

import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;

import java.util.UUID;

public record ListProductCategoryOutput(UUID id, String description) {

    public static ListProductCategoryOutput from(final ProductCategory entity) {
        return new ListProductCategoryOutput(entity.getId().id(), entity.getDescription());
    }
}
