package br.gov.pr.idr.application.inventory_resource.product.update;

import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        String name,
        String description,
        String applicationWay,
        UUID activePrincipleId,
        UUID categoryId
) {

    public static UpdateProductCommand from(UUID productId,
                                             String name,
                                             String description,
                                             String applicationWay,
                                             UUID activePrincipleId,
                                             UUID categoryId) {
        return new UpdateProductCommand(productId, name, description, applicationWay, activePrincipleId, categoryId);
    }
}
