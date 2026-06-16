package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.math.BigDecimal;
import java.util.List;

public class Property extends AggregateRoot<PropertyID> {

    private final String name;
    private BigDecimal totalArea;
    private boolean isLeased;
    private final BigDecimal nakedAveragePrice;
    private final BigDecimal leaseAveragePrice;
    private final Double dairyCattleFarmingArea;
    private final Double perennialPastureArea;
    private final Double summerPlowingArea;
    private final Double winterPlowingArea;
    private final UserID producerId;
    private final CityID cityId;
    private final Coord coord;
    private final List<PropertyCollaborator> collaborators;
    private final List<UserID> technicianIds;

    protected Property(final PropertyID id,
                       final String name,
                       final Coord coord,
                       final BigDecimal totalArea,
                       final Boolean isLeased,
                       final BigDecimal nakedAveragePrice,
                       final BigDecimal leaseAveragePrice,
                       final Double dairyCattleFarmingArea,
                       final Double perennialPastureArea,
                       final Double summerPlowingArea,
                       final Double winterPlowingArea,
                       final UserID producerId,
                       final CityID cityId,
                       final List<PropertyCollaborator> collaborators,
                       final List<UserID> technicianIds
    ) {
        super(id);
        this.name = name;
        this.coord = coord;
        this.totalArea = totalArea;
        this.isLeased = isLeased;
        this.nakedAveragePrice = nakedAveragePrice;
        this.leaseAveragePrice = leaseAveragePrice;
        this.dairyCattleFarmingArea = dairyCattleFarmingArea;
        this.perennialPastureArea = perennialPastureArea;
        this.summerPlowingArea = summerPlowingArea;
        this.winterPlowingArea = winterPlowingArea;
        this.producerId = producerId;
        this.cityId = cityId;
        this.collaborators = collaborators;
        this.technicianIds = technicianIds;
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
                                  final CityID cityId,
                                  final List<UserID> technicianIds,
                                  final List<PropertyCollaborator> collaborators
    ) {
        return new Property(PropertyID.unique(), name, coord, totalArea, leased, nakedAveragePrice, leaseAveragePrice,
                            dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing, producerId,
                            cityId, collaborators, technicianIds);
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
                                final CityID cityId,
                                final List<UserID> technicianIds,
                                final List<PropertyCollaborator> collaborators
    ) {
        return new Property(id, name, coord, totalArea, leased, nakedAveragePrice, leaseAveragePrice,
                dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing, producerId,
                cityId, collaborators, technicianIds);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("Nome da propriedade não pode ser vazio"));
        }
        if (this.cityId == null) {
            handler.append(DomainError.from("Cidade da propriedade deve ser informada"));
        }
        if (this.producerId == null) {
            handler.append(DomainError.from("Produtor da propriedade deve ser informado"));
        }
        if (this.nakedAveragePrice == null || this.nakedAveragePrice.compareTo(BigDecimal.ZERO) < 0) {
            handler.append(DomainError.from("Preço médio para arrendamento nu deve ser maior ou igual a zero"));
        }
        if (this.leaseAveragePrice == null || this.leaseAveragePrice.compareTo(BigDecimal.ZERO) < 0) {
            handler.append(DomainError.from("Preço médio para arrendamento deve ser maior ou igual a zero"));
        }
        this.validateAreaFields(handler);
    }

    private void validateAreaFields(ValidationHandler handler) {
        final var zero = 0.0;
        if (this.dairyCattleFarmingArea == null || this.dairyCattleFarmingArea.compareTo(zero) < 0) {
            handler.append(DomainError.from("Área de pecuária leiteira deve ser maior ou igual a zero"));
        }
        if (this.perennialPastureArea == null || this.perennialPastureArea.compareTo(zero) < 0) {
            handler.append(DomainError.from("Área de pastagem perene deve ser maior ou igual a zero"));
        }
        if (this.summerPlowingArea == null || this.summerPlowingArea.compareTo(zero) < 0) {
            handler.append(DomainError.from("Área de plantio de verão deve ser maior ou igual a zero"));
        }
        if (this.winterPlowingArea == null || this.winterPlowingArea.compareTo(zero) < 0) {
            handler.append(DomainError.from("Área de plantio de inverno deve ser maior ou igual a zero"));
        }
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
        return isLeased;
    }

    public BigDecimal getNakedAveragePrice() {
        return nakedAveragePrice;
    }

    public BigDecimal getLeaseAveragePrice() {
        return leaseAveragePrice;
    }

    public Double getDairyCattleFarmingArea() {
        return dairyCattleFarmingArea;
    }

    public Double getPerennialPastureArea() {
        return perennialPastureArea;
    }

    public Double getSummerPlowingArea() {
        return summerPlowingArea;
    }

    public Double getWinterPlowingArea() {
        return winterPlowingArea;
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

    public List<UserID> getTechnicianIds() {
        return technicianIds;
    }
}
