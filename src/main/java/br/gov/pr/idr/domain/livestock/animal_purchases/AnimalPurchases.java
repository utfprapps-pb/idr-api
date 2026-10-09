package br.gov.pr.idr.domain.livestock.animal_purchases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnimalPurchases extends AggregateRoot<AnimalPurchasesID> {

    private final AnimalID animalId;
    private LocalDate datePurchase;
    private LocalDate birthDate;
    private BigDecimal value;
    private Long version;

    protected AnimalPurchases(final AnimalPurchasesID id, final AnimalID animalId,
                               final LocalDate datePurchase, final LocalDate birthDate,
                               final BigDecimal value) {
        super(id);
        this.animalId = animalId;
        this.datePurchase = datePurchase;
        this.birthDate = birthDate;
        this.value = value;
    }

    public static AnimalPurchases with(final AnimalPurchasesID id, final AnimalID animalId,
                                        final LocalDate datePurchase, final LocalDate birthDate,
                                        final BigDecimal value) {
        return new AnimalPurchases(id, animalId, datePurchase, birthDate, value);
    }

    public static AnimalPurchases create(final AnimalID animalId, final LocalDate datePurchase,
                                          final LocalDate birthDate, final BigDecimal value) {
        final var animalPurchases = new AnimalPurchases(AnimalPurchasesID.unique(), animalId, datePurchase,
                birthDate, value);
        animalPurchases.selfValidate();
        return animalPurchases;
    }

    public AnimalPurchases update(final LocalDate datePurchase, final LocalDate birthDate,
                                   final BigDecimal value) {
        this.datePurchase = datePurchase;
        this.birthDate = birthDate;
        this.value = value;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da compra é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public LocalDate getDatePurchase() {
        return datePurchase;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Long getVersion() {
        return version;
    }

    public AnimalPurchases withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
