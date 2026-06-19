package br.gov.pr.idr.domain.property_management.producer;

import br.gov.pr.idr.domain.shared.Identifier;

import java.util.Objects;
import java.util.UUID;

public record ProducerID(UUID id) implements Identifier {

    public ProducerID {
        Objects.requireNonNull(id, "id não pode ser nulo");
    }

    public static ProducerID from(final UUID id) {
        return new ProducerID(id);
    }

    public static ProducerID unique() {
        return new ProducerID(UUID.randomUUID());
    }
}
