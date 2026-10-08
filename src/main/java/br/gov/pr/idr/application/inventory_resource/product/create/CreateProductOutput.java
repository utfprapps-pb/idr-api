package br.gov.pr.idr.application.inventory_resource.product.create;

import br.gov.pr.idr.domain.inventory_resource.product.Product;

import java.util.UUID;

public record CreateProductOutput(UUID id, String name) {

    public static CreateProductOutput from(final Product entity) {
        return new CreateProductOutput(entity.getId().id(), entity.getName());
    }
}
