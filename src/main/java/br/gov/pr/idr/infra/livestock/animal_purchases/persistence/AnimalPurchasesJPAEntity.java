package br.gov.pr.idr.infra.livestock.animal_purchases.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesID;
import br.gov.pr.idr.infra.shared.persistence.SyncableJPAEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "livestock_animal_purchases")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalPurchasesJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "date_purchase")
    private LocalDate datePurchase;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "weight")
    private BigDecimal weight;

    public static AnimalPurchasesJPAEntity from(final AnimalPurchases animalPurchases) {
        final var entity = new AnimalPurchasesJPAEntity(
                animalPurchases.getId().id(),
                animalPurchases.getAnimalId().id(),
                animalPurchases.getDatePurchase(),
                animalPurchases.getBirthDate(),
                animalPurchases.getValue(),
                animalPurchases.getWeight()
        );
        entity.applyVersion(animalPurchases.getVersion());
        return entity;
    }

    public AnimalPurchases toAggregate() {
        final var aggregate = AnimalPurchases.with(
                AnimalPurchasesID.from(this.id),
                AnimalID.from(this.animalId),
                this.datePurchase,
                this.birthDate,
                this.value,
                this.weight
        );
        return aggregate.withVersion(this.getVersion());
    }
}
