package br.gov.pr.idr.domain.livestock.animal_sales;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnimalSales extends AggregateRoot<AnimalSalesID> {

    private final AnimalID animalId;
    private LocalDate dateSale;
    private Reason reason;
    private BigDecimal value;
    private Destination destination;

    protected AnimalSales(final AnimalSalesID id, final AnimalID animalId, final LocalDate dateSale,
                           final Reason reason, final BigDecimal value, final Destination destination) {
        super(id);
        this.animalId = animalId;
        this.dateSale = dateSale;
        this.reason = reason;
        this.value = value;
        this.destination = destination;
    }

    public static AnimalSales with(final AnimalSalesID id, final AnimalID animalId, final LocalDate dateSale,
                                    final Reason reason, final BigDecimal value, final Destination destination) {
        return new AnimalSales(id, animalId, dateSale, reason, value, destination);
    }

    public static AnimalSales create(final AnimalID animalId, final LocalDate dateSale, final Reason reason,
                                      final BigDecimal value, final Destination destination) {
        final var animalSales = new AnimalSales(AnimalSalesID.unique(), animalId, dateSale, reason, value,
                destination);
        animalSales.selfValidate();
        return animalSales;
    }

    public AnimalSales update(final LocalDate dateSale, final Reason reason, final BigDecimal value,
                               final Destination destination) {
        this.dateSale = dateSale;
        this.reason = reason;
        this.value = value;
        this.destination = destination;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal da venda é obrigatório"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public LocalDate getDateSale() {
        return dateSale;
    }

    public Reason getReason() {
        return reason;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Destination getDestination() {
        return destination;
    }
}
