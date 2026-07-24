package br.gov.pr.idr.infra.property_management.city.persistence;

import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.infra.shared.persistence.SyncableJPAEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity(name = "City")
@Table(name = "city")
@SQLRestriction("deleted_at is null")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Getter
@Setter
public class CityJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private State state;

    @Column(name = "region_id", nullable = false)
    private UUID regionId;

    public static CityJPAEntity from(final City entity) {
        return new CityJPAEntity(
                entity.getId().id(),
                entity.getName(),
                entity.getState(),
                entity.getRegionId().id()
        );
    }

    public City toAggregate() {
        return City.with(
                CityID.from(this.id),
                this.name,
                this.state,
                RegionID.from(this.regionId)
        );
    }
}
