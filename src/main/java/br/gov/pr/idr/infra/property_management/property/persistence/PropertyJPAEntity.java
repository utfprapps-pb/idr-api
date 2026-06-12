package br.gov.pr.idr.infra.property_management.property.persistence;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "Property")
@Table(name = "property")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PropertyJPAEntity {

    @Id
    @Column(nullable = false)
    private UUID id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal totalArea;
    private Boolean leased;
    private BigDecimal nakedAveragePrice;
    private BigDecimal leaseAveragePrice;
    private Double dairyCattleFarming;
    private Double perennialPasture;
    private Double summerPlowing;
    private Double winterPlowing;

    @JoinColumn(name = "producer_id", nullable = false)
    private UUID producerId;

    @JoinColumn(name = "city_id", nullable = false)
    private UUID cityId;

    public static PropertyJPAEntity fromDomain(final Property property) {
        return new PropertyJPAEntity(
                property.getId().id(),
                property.getName(),
                property.getCoord().latitude(),
                property.getCoord().longitude(),
                property.getTotalArea(),
                property.getLeased(),
                property.getNakedAveragePrice(),
                property.getLeaseAveragePrice(),
                property.getDairyCattleFarming(),
                property.getPerennialPasture(),
                property.getSummerPlowing(),
                property.getWinterPlowing(),
                property.getProducerId().id(),
                property.getCityId().id()
        );
    }

    public Property toDomain() {
        return Property.with(
                PropertyID.from(this.id),
                this.name,
                Coord.from(this.latitude, this.longitude),
                this.totalArea,
                this.leased,
                this.nakedAveragePrice,
                this.leaseAveragePrice,
                this.dairyCattleFarming,
                this.perennialPasture,
                this.summerPlowing,
                this.winterPlowing,
                UserID.from(this.producerId),
                CityID.from(this.cityId)
        );
    }
}
