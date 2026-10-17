package br.gov.pr.idr.infra.livestock.animal.persistence;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
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
@Table(name = "livestock_animal")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "property_id", nullable = false)
    private UUID propertyId;

    @Column(name = "animal_mother_id")
    private UUID animalMotherId;

    @Column(name = "breed_id", nullable = false)
    private UUID breedId;

    private String type;

    @Column(nullable = false)
    private String identifier;

    private String gender;

    private String bornCondition;

    private LocalDate bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;

    public static AnimalJPAEntity from(final Animal animal) {
        final var entity = new AnimalJPAEntity(
                animal.getId().id(),
                animal.getPropertyId().id(),
                animal.getAnimalMotherId().map(AnimalID::id).orElse(null),
                animal.getBreedId().id(),
                animal.getType(),
                animal.getIdentifier(),
                animal.getGender(),
                animal.getBornCondition(),
                animal.getBornDate(),
                animal.getBornWeight(),
                animal.getPreviousWeight(),
                animal.getCurrentWeight(),
                animal.getEcc()
        );
        entity.applyVersion(animal.getVersion());
        return entity;
    }

    public Animal toAggregate() {
        final var aggregate = Animal.with(
                AnimalID.from(this.id),
                PropertyID.from(this.propertyId),
                this.animalMotherId == null ? null : AnimalID.from(this.animalMotherId),
                BreedID.from(this.breedId),
                this.type,
                this.identifier,
                this.gender,
                this.bornCondition,
                this.bornDate,
                this.bornWeight,
                this.previousWeight,
                this.currentWeight,
                this.ecc
        );
        return aggregate.withVersion(this.getVersion());
    }
}
