package br.gov.pr.idr.application.inventory_resource.product_category.update;

import java.util.UUID;

public record UpdateProductCategoryCommand(UUID id, String description) {
}
