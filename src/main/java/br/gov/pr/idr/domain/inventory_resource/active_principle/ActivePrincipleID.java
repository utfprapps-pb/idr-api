package br.gov.pr.idr.domain.inventory_resource.active_principle;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record ActivePrincipleID(UUID id) implements Identifier {

    public ActivePrincipleID {
        Objects.requireNonNull(id, "ActivePrincipleID não pode ser nulo");
    }

    public static ActivePrincipleID unique() {
        return from(UUID.randomUUID());
    }

    public static ActivePrincipleID from(final UUID id) {
        return new ActivePrincipleID(id);
    }

}
