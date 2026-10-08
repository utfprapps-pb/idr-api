package br.gov.pr.idr.infra.livestock.insemination.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
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
@Table(name = "livestock_insemination")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InseminationJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "ia_date")
    private LocalDate iaDate;

    private String bull;

    public static InseminationJPAEntity from(final Insemination insemination) {
        return new InseminationJPAEntity(
                insemination.getId().id(),
                insemination.getAnimalId().id(),
                insemination.getIaDate(),
                insemination.getBull()
        );
    }

    public Insemination toAggregate() {
        return Insemination.with(
                InseminationID.from(this.id),
                AnimalID.from(this.animalId),
                this.iaDate,
                this.bull
        );
    }
}
