package br.gov.pr.idr.application.inventory_resource.product.update;

import br.gov.pr.idr.domain.inventory_resource.product.Product;

import java.util.UUID;

public record UpdateProductOutput(UUID id, String name, String description, String applicationWay,
                                   UUID activePrincipleId, UUID categoryId) {

    public static UpdateProductOutput from(final Product entity) {
        return new UpdateProductOutput(entity.getId().id(), entity.getName(), entity.getDescription(),
                entity.getApplicationWay(), entity.getActivePrincipleId().id(), entity.getCategoryId().id());
    }
}
