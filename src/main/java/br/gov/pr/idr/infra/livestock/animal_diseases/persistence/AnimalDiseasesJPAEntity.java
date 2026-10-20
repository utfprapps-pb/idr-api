package br.gov.pr.idr.infra.livestock.animal_diseases.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesID;
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
@Table(name = "livestock_animal_diseases")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalDiseasesJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "diagnosis_date")
    private LocalDate diagnosisDate;

    public static AnimalDiseasesJPAEntity from(final AnimalDiseases animalDiseases) {
        final var entity = new AnimalDiseasesJPAEntity(
                animalDiseases.getId().id(),
                animalDiseases.getAnimalId().id(),
                animalDiseases.getDiagnosis(),
                animalDiseases.getDiagnosisDate()
        );
        entity.applyVersion(animalDiseases.getVersion());
        return entity;
    }

    public AnimalDiseases toAggregate() {
        final var aggregate = AnimalDiseases.with(
                AnimalDiseasesID.from(this.id),
                AnimalID.from(this.animalId),
                this.diagnosis,
                this.diagnosisDate
        );
        return aggregate.withVersion(this.getVersion());
    }
}
