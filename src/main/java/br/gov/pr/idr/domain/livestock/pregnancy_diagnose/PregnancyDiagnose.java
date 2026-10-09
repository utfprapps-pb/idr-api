package br.gov.pr.idr.domain.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

public class PregnancyDiagnose extends AggregateRoot<PregnancyDiagnoseID> {

    private final AnimalID animalId;
    private LocalDate diagnoseDate;
    private LocalDate lastIa;
    private Long version;

    protected PregnancyDiagnose(final PregnancyDiagnoseID id, final AnimalID animalId,
                                 final LocalDate diagnoseDate, final LocalDate lastIa) {
        super(id);
        this.animalId = animalId;
        this.diagnoseDate = diagnoseDate;
        this.lastIa = lastIa;
    }

    public static PregnancyDiagnose with(final PregnancyDiagnoseID id, final AnimalID animalId,
                                          final LocalDate diagnoseDate, final LocalDate lastIa) {
        return new PregnancyDiagnose(id, animalId, diagnoseDate, lastIa);
    }

    public static PregnancyDiagnose create(final AnimalID animalId, final LocalDate diagnoseDate,
                                            final LocalDate lastIa) {
        final var pregnancyDiagnose = new PregnancyDiagnose(PregnancyDiagnoseID.unique(), animalId, diagnoseDate,
                lastIa);
        pregnancyDiagnose.selfValidate();
        return pregnancyDiagnose;
    }

    public PregnancyDiagnose update(final LocalDate diagnoseDate, final LocalDate lastIa) {
        this.diagnoseDate = diagnoseDate;
        this.lastIa = lastIa;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal do diagnóstico de gestação é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public LocalDate getDiagnoseDate() {
        return diagnoseDate;
    }

    public LocalDate getLastIa() {
        return lastIa;
    }

    public Long getVersion() {
        return version;
    }

    public PregnancyDiagnose withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
