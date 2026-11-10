package br.gov.pr.idr.domain.inventory_resource.product;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record ProductID(UUID id) implements Identifier {

    public ProductID {
        Objects.requireNonNull(id, "ProductID não pode ser nulo");
    }

    public static ProductID unique() {
        return from(UUID.randomUUID());
    }

    public static ProductID from(final UUID id) {
        return new ProductID(id);
    }
}
