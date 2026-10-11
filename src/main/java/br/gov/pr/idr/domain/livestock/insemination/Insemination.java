package br.gov.pr.idr.domain.livestock.insemination;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

/** Inseminação artificial de um animal. O touro é texto livre, não referencia um animal do rebanho. */
public class Insemination extends AggregateRoot<InseminationID> {

    private final AnimalID animalId;
    private LocalDate iaDate;
    private String bull;
    private Long version;

    protected Insemination(final InseminationID id, final AnimalID animalId, final LocalDate iaDate,
                            final String bull) {
        super(id);
        this.animalId = animalId;
        this.iaDate = iaDate;
        this.bull = bull;
    }

    public static Insemination with(final InseminationID id, final AnimalID animalId, final LocalDate iaDate,
                                     final String bull) {
        return new Insemination(id, animalId, iaDate, bull);
    }

    public static Insemination create(final AnimalID animalId, final LocalDate iaDate, final String bull) {
        final var insemination = new Insemination(InseminationID.unique(), animalId, iaDate, bull);
        insemination.selfValidate();
        return insemination;
    }

    public Insemination update(final LocalDate iaDate, final String bull) {
        this.iaDate = iaDate;
        this.bull = bull;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da inseminação é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public LocalDate getIaDate() {
        return iaDate;
    }

    public String getBull() {
        return bull;
    }

    public Long getVersion() {
        return version;
    }

    public Insemination withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
