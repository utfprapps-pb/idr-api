package br.gov.pr.idr.infra.livestock.mastitis.persistence;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
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
@Table(name = "livestock_mastitis")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MastitisJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private UUID animalId;

    @Column(name = "diagnose_date")
    private LocalDate diagnoseDate;

    private String mastitisType;

    private String cmtResult;

    private String ad;

    private String ae;

    private String pd;

    private String pe;

    public static MastitisJPAEntity from(final Mastitis mastitis) {
        return new MastitisJPAEntity(
                mastitis.getId().id(),
                mastitis.getAnimalId().id(),
                mastitis.getDiagnoseDate(),
                mastitis.getMastitisType(),
                mastitis.getCmtResult(),
                mastitis.getAd(),
                mastitis.getAe(),
                mastitis.getPd(),
                mastitis.getPe()
        );
    }

    public Mastitis toAggregate() {
        return Mastitis.with(
                MastitisID.from(this.id),
                AnimalID.from(this.animalId),
                this.diagnoseDate,
                this.mastitisType,
                this.cmtResult,
                this.ad,
                this.ae,
                this.pd,
                this.pe
        );
    }
}
