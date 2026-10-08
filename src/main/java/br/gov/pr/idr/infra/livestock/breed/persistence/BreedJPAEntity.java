package br.gov.pr.idr.infra.livestock.breed.persistence;

import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
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
@Table(name = "livestock_breed")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BreedJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String breedName;

    public static BreedJPAEntity from(final Breed breed) {
        return new BreedJPAEntity(
                breed.getId().id(),
                breed.getBreedName()
        );
    }

    public Breed toAggregate() {
        return Breed.with(
                BreedID.from(this.id),
                this.breedName
        );
    }
}
