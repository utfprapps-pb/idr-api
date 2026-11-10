package br.gov.pr.idr.domain.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.shared.tactical.Identifier;

import java.util.Objects;
import java.util.UUID;

public record PregnancyDiagnoseID(UUID id) implements Identifier {

    public PregnancyDiagnoseID {
        Objects.requireNonNull(id, "PregnancyDiagnoseID não pode ser nulo");
    }

    public static PregnancyDiagnoseID unique() {
        return from(UUID.randomUUID());
    }

    public static PregnancyDiagnoseID from(final UUID id) {
        return new PregnancyDiagnoseID(id);
    }
}
