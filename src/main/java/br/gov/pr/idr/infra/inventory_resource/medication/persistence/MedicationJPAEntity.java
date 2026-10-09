package br.gov.pr.idr.infra.inventory_resource.medication.persistence;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
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

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "inventory_resource_medication")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicationJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "applied_dose")
    private String appliedDose;

    @Column(name = "application_way")
    private String applicationWay;

    public static MedicationJPAEntity from(final Medication medication) {
        final var entity = new MedicationJPAEntity(
                medication.getId().id(),
                medication.getAnimalId().id(),
                medication.getProductId().id(),
                medication.getApplicationDate(),
                medication.getAppliedDose(),
                medication.getApplicationWay()
        );
        entity.applyVersion(medication.getVersion());
        return entity;
    }

    public Medication toAggregate() {
        final var aggregate = Medication.with(
                MedicationID.from(this.id),
                AnimalID.from(this.animalId),
                ProductID.from(this.productId),
                this.applicationDate,
                this.appliedDose,
                this.applicationWay
        );
        return aggregate.withVersion(this.getVersion());
    }
}
