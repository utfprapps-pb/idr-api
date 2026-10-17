package br.gov.pr.idr.infra.inventory_resource.product.models.update;

import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductOutput;

import java.util.UUID;

public record UpdateProductResponse(UUID id, String name, String description, String applicationWay,
                                     UUID activePrincipleId, UUID categoryId) {

    public static UpdateProductResponse from(final UpdateProductOutput output) {
        return new UpdateProductResponse(output.id(), output.name(), output.description(),
                output.applicationWay(), output.activePrincipleId(), output.categoryId());
    }
}
