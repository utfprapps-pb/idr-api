package br.gov.pr.idr.infra.inventory_resource.product.models;

import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductOutput;

import java.util.UUID;

public record CreateProductResponse(UUID id, String name) {

    public static CreateProductResponse from(final CreateProductOutput output) {
        return new CreateProductResponse(output.id(), output.name());
    }
}
