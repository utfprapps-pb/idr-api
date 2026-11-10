package br.gov.pr.idr.application.inventory_resource.product.create;

import java.util.UUID;

public record CreateProductCommand(
        String name,
        String description,
        String applicationWay,
        UUID activePrincipleId,
        UUID categoryId
) {

    public static CreateProductCommand from(String name,
                                             String description,
                                             String applicationWay,
                                             UUID activePrincipleId,
                                             UUID categoryId) {
        return new CreateProductCommand(name, description, applicationWay, activePrincipleId, categoryId);
    }
}
