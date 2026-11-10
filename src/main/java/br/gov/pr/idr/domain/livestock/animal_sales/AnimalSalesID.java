package br.gov.pr.idr.domain.livestock.animal_sales;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalSalesID(UUID id) implements Identifier {

    public AnimalSalesID {
        Objects.requireNonNull(id, "AnimalSalesID não pode ser nulo");
    }

    public static AnimalSalesID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalSalesID from(final UUID id) {
        return new AnimalSalesID(id);
    }
}
