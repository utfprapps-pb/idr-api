package br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
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
@Table(name = "livestock_pregnancy_diagnose")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PregnancyDiagnoseJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "diagnose_date")
    private LocalDate diagnoseDate;

    @Column(name = "lastia")
    private LocalDate lastIa;

    public static PregnancyDiagnoseJPAEntity from(final PregnancyDiagnose pregnancyDiagnose) {
        final var entity = new PregnancyDiagnoseJPAEntity(
                pregnancyDiagnose.getId().id(),
                pregnancyDiagnose.getAnimalId().id(),
                pregnancyDiagnose.getDiagnoseDate(),
                pregnancyDiagnose.getLastIa()
        );
        entity.applyVersion(pregnancyDiagnose.getVersion());
        return entity;
    }

    public PregnancyDiagnose toAggregate() {
        final var aggregate = PregnancyDiagnose.with(
                PregnancyDiagnoseID.from(this.id),
                AnimalID.from(this.animalId),
                this.diagnoseDate,
                this.lastIa
        );
        return aggregate.withVersion(this.getVersion());
    }
}
