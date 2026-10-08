package br.gov.pr.idr.domain.inventory_resource.product.query;

import java.util.UUID;

public record ListProductQueryResult(UUID id,
                                      String name,
                                      String description,
                                      String applicationWay,
                                      ActivePrincipleRef activePrinciple,
                                      ProductCategoryRef category) {

    public record ActivePrincipleRef(UUID id, String name) {
    }

    public record ProductCategoryRef(UUID id, String description) {
    }

    public ListProductQueryResult(UUID id, String name, String description, String applicationWay,
                                   UUID activePrincipleId, String activePrincipleName,
                                   UUID categoryId, String categoryDescription) {
        this(id, name, description, applicationWay,
                new ActivePrincipleRef(activePrincipleId, activePrincipleName),
                new ProductCategoryRef(categoryId, categoryDescription));
    }
}
