package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.technician.PropertyTechnician;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.math.BigDecimal;
import java.util.List;

public class Property extends AggregateRoot<PropertyID> {

    private String name;
    private Coord coord;
    private BigDecimal totalArea;
    private Boolean leased;
    private BigDecimal nakedAveragePrice;
    private BigDecimal leaseAveragePrice;
    private Double dairyCattleFarming;
    private Double perennialPasture;
    private Double summerPlowing;
    private Double winterPlowing;
    private UserID producerId;
    private CityID cityId;
    private List<PropertyCollaborator> collaborators;
    private List<PropertyTechnician> technicians;

    protected Property(final PropertyID id,
                       final String name,
                       final Coord coord,
                       final BigDecimal totalArea,
                       final Boolean leased,
                       final BigDecimal nakedAveragePrice,
                       final BigDecimal leaseAveragePrice,
                       final Double dairyCattleFarming,
                       final Double perennialPasture,
                       final Double summerPlowing,
                       final Double winterPlowing,
                       final UserID producerId,
                       final CityID cityId,
                       final List<PropertyCollaborator> collaborators,
                       final List<PropertyTechnician> technicians
    ) {
        super(id);
        this.name = name;
        this.coord = coord;
        this.totalArea = totalArea;
        this.leased = leased;
        this.nakedAveragePrice = nakedAveragePrice;
        this.leaseAveragePrice = leaseAveragePrice;
        this.dairyCattleFarming = dairyCattleFarming;
        this.perennialPasture = perennialPasture;
        this.summerPlowing = summerPlowing;
        this.winterPlowing = winterPlowing;
        this.producerId = producerId;
        this.cityId = cityId;
        this.collaborators = collaborators;
        this.technicians = technicians;
        selfValidate();
    }

    public static Property create(final String name,
                                  final Coord coord,
                                  final BigDecimal totalArea,
                                  final Boolean leased,
                                  final BigDecimal nakedAveragePrice,
                                  final BigDecimal leaseAveragePrice,
                                  final Double dairyCattleFarming,
                                  final Double perennialPasture,
                                  final Double summerPlowing,
                                  final Double winterPlowing,
                                  final UserID producerId,
                                  final CityID cityId
    ) {
        return new Property(PropertyID.unique(), name, coord, totalArea, leased, nakedAveragePrice, leaseAveragePrice, dairyCattleFarming, perennialPasture, summerPlowing
                , winterPlowing, producerId, cityId, null, null);
    }

    public static Property with(final PropertyID id,
                                final String name,
                                final Coord coord,
                                final BigDecimal totalArea,
                                final Boolean leased,
                                final BigDecimal nakedAveragePrice,
                                final BigDecimal leaseAveragePrice,
                                final Double dairyCattleFarming,
                                final Double perennialPasture,
                                final Double summerPlowing,
                                final Double winterPlowing,
                                final UserID producerId,
                                final CityID cityId
    ) {
        return new Property(id, name, coord, totalArea, leased, nakedAveragePrice, leaseAveragePrice, dairyCattleFarming, perennialPasture, summerPlowing
                , winterPlowing, producerId, cityId, null, null);
    }

    @Override
    public void validate(ValidationHandler handler) {

    }

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public Boolean getLeased() {
        return leased;
    }

    public BigDecimal getNakedAveragePrice() {
        return nakedAveragePrice;
    }

    public BigDecimal getLeaseAveragePrice() {
        return leaseAveragePrice;
    }

    public Double getDairyCattleFarming() {
        return dairyCattleFarming;
    }

    public Double getPerennialPasture() {
        return perennialPasture;
    }

    public Double getSummerPlowing() {
        return summerPlowing;
    }

    public Double getWinterPlowing() {
        return winterPlowing;
    }

    public UserID getProducerId() {
        return producerId;
    }

    public CityID getCityId() {
        return cityId;
    }

    public List<PropertyCollaborator> getCollaborators() {
        return collaborators;
    }

    public List<PropertyTechnician> getTechnicians() {
        return technicians;
    }
}
