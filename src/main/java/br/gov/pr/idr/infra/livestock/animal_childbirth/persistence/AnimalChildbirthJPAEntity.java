package br.gov.pr.idr.infra.livestock.animal_childbirth.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
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
@Table(name = "livestock_animal_childbirth")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalChildbirthJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "childbirth_date")
    private LocalDate childbirthDate;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private ChildbirthGender gender;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "birth_condition")
    @Enumerated(EnumType.STRING)
    private ChildbirthCondition condition;

    @Column(name = "breed_id", nullable = false)
    private UUID breedId;

    @Column(name = "calf_id")
    private UUID calfId;

    public static AnimalChildbirthJPAEntity from(final AnimalChildbirth animalChildbirth) {
        final var entity = new AnimalChildbirthJPAEntity(
                animalChildbirth.getId().id(),
                animalChildbirth.getAnimalId().id(),
                animalChildbirth.getChildbirthDate(),
                animalChildbirth.getGender(),
                animalChildbirth.getWeight(),
                animalChildbirth.getCondition(),
                animalChildbirth.getBreedId().id(),
                animalChildbirth.getCalfId().map(AnimalID::id).orElse(null)
        );
        entity.applyVersion(animalChildbirth.getVersion());
        return entity;
    }

    public AnimalChildbirth toAggregate() {
        final var aggregate = AnimalChildbirth.with(
                AnimalChildbirthID.from(this.id),
                AnimalID.from(this.animalId),
                this.childbirthDate,
                this.gender,
                this.weight,
                this.condition,
                BreedID.from(this.breedId),
                this.calfId == null ? null : AnimalID.from(this.calfId)
        );
        return aggregate.withVersion(this.getVersion());
    }
}
