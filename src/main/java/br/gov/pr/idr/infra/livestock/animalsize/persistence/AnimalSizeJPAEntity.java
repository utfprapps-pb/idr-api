package br.gov.pr.idr.infra.livestock.animalsize.persistence;

import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
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

import java.util.UUID;

@Entity
@Table(name = "livestock_animal_size")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimalSizeJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String sizeName;

    public static AnimalSizeJPAEntity from(final AnimalSize animalSize) {
        final var entity = new AnimalSizeJPAEntity(
                animalSize.getId().id(),
                animalSize.getSizeName()
        );
        entity.applyVersion(animalSize.getVersion());
        return entity;
    }

    public AnimalSize toAggregate() {
        final var aggregate = AnimalSize.with(
                AnimalSizeID.from(this.id),
                this.sizeName
        );
        return aggregate.withVersion(this.getVersion());
    }
}
