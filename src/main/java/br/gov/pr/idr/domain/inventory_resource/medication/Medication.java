package br.gov.pr.idr.domain.inventory_resource.medication;

import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

/**
 * Aplicação de um produto num animal: produto, data, dose e via de aplicação. Mora em {@code inventory_resource},
 * junto do produto, mas o acesso é controlado pela propriedade do animal ({@code livestock}).
 */
public class Medication extends AggregateRoot<MedicationID> {

    private final AnimalID animalId;
    private ProductID productId;
    private LocalDate applicationDate;
    private String appliedDose;
    private String applicationWay;
    private Long version;

    protected Medication(final MedicationID id, final AnimalID animalId, final ProductID productId,
                          final LocalDate applicationDate, final String appliedDose, final String applicationWay) {
        super(id);
        this.animalId = animalId;
        this.productId = productId;
        this.applicationDate = applicationDate;
        this.appliedDose = appliedDose;
        this.applicationWay = applicationWay;
    }

    public static Medication with(final MedicationID id, final AnimalID animalId, final ProductID productId,
                                   final LocalDate applicationDate, final String appliedDose,
                                   final String applicationWay) {
        return new Medication(id, animalId, productId, applicationDate, appliedDose, applicationWay);
    }

    public static Medication create(final AnimalID animalId, final ProductID productId,
                                      final LocalDate applicationDate, final String appliedDose,
                                      final String applicationWay) {
        final var medication = new Medication(MedicationID.unique(), animalId, productId, applicationDate,
                appliedDose, applicationWay);
        medication.selfValidate();
        return medication;
    }

    public Medication update(final ProductID productId, final LocalDate applicationDate, final String appliedDose,
                              final String applicationWay) {
        this.productId = productId;
        this.applicationDate = applicationDate;
        this.appliedDose = appliedDose;
        this.applicationWay = applicationWay;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da medicação é obrigatório"));
        }
        if (this.productId == null) {
            handler.append(DomainError.from("O produto da medicação é obrigatório"));
        }
        if (this.appliedDose == null || this.appliedDose.isBlank()) {
            handler.append(DomainError.from("A dose aplicada da medicação não pode ser vazia"));
        }
        if (this.applicationWay == null || this.applicationWay.isBlank()) {
            handler.append(DomainError.from("A via de aplicação da medicação não pode ser vazia"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public ProductID getProductId() {
        return productId;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getAppliedDose() {
        return appliedDose;
    }

    public String getApplicationWay() {
        return applicationWay;
    }

    public Long getVersion() {
        return version;
    }

    public Medication withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
