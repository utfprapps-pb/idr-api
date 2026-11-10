package br.gov.pr.idr.domain.livestock.animal_purchases;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record AnimalPurchasesID(UUID id) implements Identifier {

    public AnimalPurchasesID {
        Objects.requireNonNull(id, "AnimalPurchasesID não pode ser nulo");
    }

    public static AnimalPurchasesID unique() {
        return from(UUID.randomUUID());
    }

    public static AnimalPurchasesID from(final UUID id) {
        return new AnimalPurchasesID(id);
    }
}
