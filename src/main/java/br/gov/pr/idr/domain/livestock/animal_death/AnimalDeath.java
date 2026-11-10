package br.gov.pr.idr.domain.livestock.animal_death;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

/** Morte de um animal, com causa e data. Cada animal tem no máximo uma (garantido no caso de uso de criação). */
public class AnimalDeath extends AggregateRoot<AnimalDeathID> {

    private final AnimalID animalId;
    private String reason;
    private LocalDate deathDate;
    private Long version;

    protected AnimalDeath(final AnimalDeathID id, final AnimalID animalId,
                          final String reason, final LocalDate deathDate) {
        super(id);
        this.animalId = animalId;
        this.reason = reason;
        this.deathDate = deathDate;
    }

    public static AnimalDeath with(final AnimalDeathID id, final AnimalID animalId,
                                   final String reason, final LocalDate deathDate) {
        return new AnimalDeath(id, animalId, reason, deathDate);
    }

    public static AnimalDeath create(final AnimalID animalId, final String reason,
                                     final LocalDate deathDate) {
        final var animalDeath = new AnimalDeath(AnimalDeathID.unique(), animalId, reason,
                deathDate);
        animalDeath.selfValidate();
        return animalDeath;
    }

    public AnimalDeath update(final String reason, final LocalDate deathDate) {
        this.reason = reason;
        this.deathDate = deathDate;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da morte é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public Long getVersion() {
        return version;
    }

    public AnimalDeath withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
