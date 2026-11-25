package br.gov.pr.idr.domain.inventory_resource.medication;

import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;

/**
 * Evento de medicação de um animal: registra o que foi aplicado (nome do produto e princípio ativo), a data, a dose
 * e a via de aplicação. O vínculo com o catálogo ({@code productId}) é opcional: quando existe, o nome e o princípio
 * ativo são uma cópia do catálogo no momento do registro; sem ele, são o texto digitado. Mora em
 * {@code inventory_resource}, junto do produto, mas o acesso é controlado pela propriedade do animal
 * ({@code livestock}).
 */
public class Medication extends AggregateRoot<MedicationID> {

    private final AnimalID animalId;
    private ProductID productId;
    private String productName;
    private String activePrinciple;
    private LocalDate applicationDate;
    private String appliedDose;
    private String applicationWay;
    private Long version;

    protected Medication(final MedicationID id, final AnimalID animalId, final ProductID productId,
                          final String productName, final String activePrinciple,
                          final LocalDate applicationDate, final String appliedDose, final String applicationWay) {
        super(id);
        this.animalId = animalId;
        this.productId = productId;
        this.productName = productName;
        this.activePrinciple = activePrinciple;
        this.applicationDate = applicationDate;
        this.appliedDose = appliedDose;
        this.applicationWay = applicationWay;
    }

    public static Medication with(final MedicationID id, final AnimalID animalId, final ProductID productId,
                                   final String productName, final String activePrinciple,
                                   final LocalDate applicationDate, final String appliedDose,
                                   final String applicationWay) {
        return new Medication(id, animalId, productId, productName, activePrinciple, applicationDate, appliedDose,
                applicationWay);
    }

    public static Medication create(final AnimalID animalId, final ProductID productId, final String productName,
                                      final String activePrinciple, final LocalDate applicationDate,
                                      final String appliedDose, final String applicationWay) {
        final var medication = new Medication(MedicationID.unique(), animalId, productId, productName,
                activePrinciple, applicationDate, appliedDose, applicationWay);
        medication.selfValidate();
        return medication;
    }

    public Medication update(final ProductID productId, final String productName, final String activePrinciple,
                              final LocalDate applicationDate, final String appliedDose,
                              final String applicationWay) {
        this.productId = productId;
        this.productName = productName;
        this.activePrinciple = activePrinciple;
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
        if (this.productName == null || this.productName.isBlank()) {
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

    /** Produto do catálogo, ou {@code null} quando o produto foi digitado. */
    public ProductID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getActivePrinciple() {
        return activePrinciple;
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
