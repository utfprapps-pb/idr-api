package br.gov.pr.idr.infra.property_management.region.persistence;

import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "region")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegionJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String description;

    public static RegionJPAEntity from(final Region region) {
        return new RegionJPAEntity(
                region.getId().id(),
                region.getDescription()
        );
    }

    public Region toAggregate() {
        return Region.with(
                RegionID.from(this.id),
                this.description
        );
    }
}
