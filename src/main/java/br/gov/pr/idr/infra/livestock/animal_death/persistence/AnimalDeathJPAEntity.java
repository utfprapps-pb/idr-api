package br.gov.pr.idr.infra.livestock.animal_death.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathID;
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
@Table(name = "livestock_animal_death")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalDeathJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "death_date")
    private LocalDate deathDate;

    public static AnimalDeathJPAEntity from(final AnimalDeath animalDeath) {
        final var entity = new AnimalDeathJPAEntity(
                animalDeath.getId().id(),
                animalDeath.getAnimalId().id(),
                animalDeath.getReason(),
                animalDeath.getDeathDate()
        );
        entity.applyVersion(animalDeath.getVersion());
        return entity;
    }

    public AnimalDeath toAggregate() {
        final var aggregate = AnimalDeath.with(
                AnimalDeathID.from(this.id),
                AnimalID.from(this.animalId),
                this.reason,
                this.deathDate
        );
        return aggregate.withVersion(this.getVersion());
    }
}
