package br.gov.pr.idr.domain.inventory_resource.medication;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record MedicationID(UUID id) implements Identifier {

    public MedicationID {
        Objects.requireNonNull(id, "MedicationID não pode ser nulo");
    }

    public static MedicationID unique() {
        return from(UUID.randomUUID());
    }

    public static MedicationID from(final UUID id) {
        return new MedicationID(id);
    }
}
