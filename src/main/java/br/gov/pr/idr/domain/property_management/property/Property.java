package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachment;
import br.gov.pr.idr.domain.property_management.property.attachment.PropertyAttachmentID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
    private final Long version;
    private Instant updatedAt;
    private final List<PropertyAttachment> attachments;

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
                       final List<UserID> technicianIds,
                       final Long version,
                       final Instant updatedAt,
                       final List<PropertyAttachment> attachments
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
        this.version = version;
        this.updatedAt = updatedAt;
        this.attachments = attachments == null ? new ArrayList<>() : new ArrayList<>(attachments);
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
        final var property = new Property(PropertyID.unique(), name, coord, nakedAveragePrice, leaseAveragePrice,
                            dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing,
                            producerId, cityId, collaborators, technicianIds, null, null, null);
        property.selfValidate();
        return property;
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
                                final List<PropertyCollaborator> collaborators,
                                final Long version,
                                final Instant updatedAt
                               ) {
        return new Property(id, name, coord, nakedAveragePrice, leaseAveragePrice,
                            dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing,
                            producerId, cityId, collaborators, technicianIds, version, updatedAt, null);
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
        this.updatedAt = Instant.now();
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

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
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

    public ProducerID getProducerId() {
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

    public Long getVersion() {
        return version;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void addAttachment(final PropertyAttachment attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachment(final PropertyAttachmentID attachmentId) {
        final var removed = this.attachments.removeIf(attachment -> attachment.getId()
                .equals(attachmentId));
        if (!removed) {
            throw new UnprocessableEntityException("Anexo não encontrado para esta propriedade");
        }
    }

    public List<PropertyAttachment> getAttachments() {
        return attachments;
    }
}
