package br.gov.pr.idr.infra.livestock.animal_sales.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesID;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
import br.gov.pr.idr.infra.shared.persistence.SyncableJPAEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "livestock_animal_sales")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalSalesJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "date_sale")
    private LocalDate dateSale;

    @Column(name = "reason")
    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "destination")
    @Enumerated(EnumType.STRING)
    private Destination destination;

    @Column(name = "weight")
    private BigDecimal weight;

    public static AnimalSalesJPAEntity from(final AnimalSales animalSales) {
        final var entity = new AnimalSalesJPAEntity(
                animalSales.getId().id(),
                animalSales.getAnimalId().id(),
                animalSales.getDateSale(),
                animalSales.getReason(),
                animalSales.getValue(),
                animalSales.getDestination(),
                animalSales.getWeight()
        );
        entity.applyVersion(animalSales.getVersion());
        return entity;
    }

    public AnimalSales toAggregate() {
        final var aggregate = AnimalSales.with(
                AnimalSalesID.from(this.id),
                AnimalID.from(this.animalId),
                this.dateSale,
                this.reason,
                this.value,
                this.destination,
                this.weight
        );
        return aggregate.withVersion(this.getVersion());
    }
}
