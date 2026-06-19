package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.math.BigDecimal;
import java.util.List;

public class Property extends AggregateRoot<PropertyID> {

    private String name;
    private Coord coord;
    private BigDecimal nakedAveragePrice;
    private BigDecimal leaseAveragePrice;
    private Double dairyCattleFarmingArea;
    private Double perennialPastureArea;
    private Double summerPlowingArea;
    private Double winterPlowingArea;
    private CityID cityId;
    private List<PropertyCollaborator> collaborators;
    private List<UserID> technicianIds;
    private ProducerID producerId;

    protected Property(final PropertyID id,
                       final String name,
                       final Coord coord,
                       final BigDecimal nakedAveragePrice,
                       final BigDecimal leaseAveragePrice,
                       final Double dairyCattleFarmingArea,
                       final Double perennialPastureArea,
                       final Double summerPlowingArea,
                       final Double winterPlowingArea,
                       final ProducerID producerId,
                       final CityID cityId,
                       final List<PropertyCollaborator> collaborators,
                       final List<UserID> technicianIds
    ) {
        super(id);
        this.name = name;
        this.coord = coord;
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
                                  final BigDecimal nakedAveragePrice,
                                  final BigDecimal leaseAveragePrice,
                                  final Double dairyCattleFarming,
                                  final Double perennialPasture,
                                  final Double summerPlowing,
                                  final Double winterPlowing,
                                  final ProducerID producerId,
                                  final CityID cityId,
                                  final List<UserID> technicianIds,
                                  final List<PropertyCollaborator> collaborators
    ) {
        return new Property(PropertyID.unique(), name, coord, nakedAveragePrice, leaseAveragePrice,
                dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing,
                producerId, cityId, collaborators, technicianIds);
    }

    public static Property with(final PropertyID id,
                                final String name,
                                final Coord coord,
                                final BigDecimal nakedAveragePrice,
                                final BigDecimal leaseAveragePrice,
                                final Double dairyCattleFarming,
                                final Double perennialPasture,
                                final Double summerPlowing,
                                final Double winterPlowing,
                                final ProducerID producerId,
                                final CityID cityId,
                                final List<UserID> technicianIds,
                                final List<PropertyCollaborator> collaborators
    ) {
        return new Property(id, name, coord, nakedAveragePrice, leaseAveragePrice,
                dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing,
                producerId, cityId, collaborators, technicianIds);
    }

    public Property update(final String name,
                           final Coord coord,
                           final BigDecimal nakedAveragePrice,
                           final BigDecimal leaseAveragePrice,
                           final Double dairyCattleFarming,
                           final Double perennialPasture,
                           final Double summerPlowing,
                           final Double winterPlowing,
                           final ProducerID producerId,
                           final CityID cityId,
                           final List<UserID> technicianIds,
                           final List<PropertyCollaborator> collaborators
    ) {
        this.name = name;
        this.coord = coord;
        this.nakedAveragePrice = nakedAveragePrice;
        this.leaseAveragePrice = leaseAveragePrice;
        this.dairyCattleFarmingArea = dairyCattleFarming;
        this.perennialPastureArea = perennialPasture;
        this.summerPlowingArea = summerPlowing;
        this.winterPlowingArea = winterPlowing;
        this.producerId = producerId;
        this.cityId = cityId;
        this.technicianIds = technicianIds;
        this.collaborators = collaborators;
        selfValidate();
        return this;
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

    public String getName() { return name; }

    public Coord getCoord() { return coord; }

    public BigDecimal getNakedAveragePrice() { return nakedAveragePrice; }

    public BigDecimal getLeaseAveragePrice() { return leaseAveragePrice; }

    public Double getDairyCattleFarmingArea() { return dairyCattleFarmingArea; }

    public Double getPerennialPastureArea() { return perennialPastureArea; }

    public Double getSummerPlowingArea() { return summerPlowingArea; }

    public Double getWinterPlowingArea() { return winterPlowingArea; }

    public ProducerID getProducerId() { return producerId; }

    public CityID getCityId() { return cityId; }

    public List<PropertyCollaborator> getCollaborators() { return collaborators; }

    public List<UserID> getTechnicianIds() { return technicianIds; }
}
