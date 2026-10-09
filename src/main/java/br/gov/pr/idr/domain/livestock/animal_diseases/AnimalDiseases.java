package br.gov.pr.idr.domain.livestock.animal_diseases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

public class AnimalDiseases extends AggregateRoot<AnimalDiseasesID> {

    private final AnimalID animalId;
    private String diagnosis;
    private LocalDate diagnosisDate;
    private Long version;

    protected AnimalDiseases(final AnimalDiseasesID id, final AnimalID animalId,
                              final String diagnosis, final LocalDate diagnosisDate) {
        super(id);
        this.animalId = animalId;
        this.diagnosis = diagnosis;
        this.diagnosisDate = diagnosisDate;
    }

    public static AnimalDiseases with(final AnimalDiseasesID id, final AnimalID animalId,
                                       final String diagnosis, final LocalDate diagnosisDate) {
        return new AnimalDiseases(id, animalId, diagnosis, diagnosisDate);
    }

    public static AnimalDiseases create(final AnimalID animalId, final String diagnosis,
                                         final LocalDate diagnosisDate) {
        final var animalDiseases = new AnimalDiseases(AnimalDiseasesID.unique(), animalId, diagnosis,
                diagnosisDate);
        animalDiseases.selfValidate();
        return animalDiseases;
    }

    public AnimalDiseases update(final String diagnosis, final LocalDate diagnosisDate) {
        this.diagnosis = diagnosis;
        this.diagnosisDate = diagnosisDate;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da doença é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    public Long getVersion() {
        return version;
    }

    public AnimalDiseases withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
