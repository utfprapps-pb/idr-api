package br.gov.pr.idr.domain.inventory_resource.product_category;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record ProductCategoryID(UUID id) implements Identifier {

    public ProductCategoryID {
        Objects.requireNonNull(id, "ProductCategoryID não pode ser nulo");
    }

    public static ProductCategoryID unique() {
        return from(UUID.randomUUID());
    }

    public static ProductCategoryID from(final UUID id) {
        return new ProductCategoryID(id);
    }

}
