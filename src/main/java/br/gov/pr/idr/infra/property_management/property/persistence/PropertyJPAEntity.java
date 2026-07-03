package br.gov.pr.idr.infra.property_management.property.persistence;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.infra.property_management.property.persistence.attachment.PropertyAttachmentJPAEntity;
import br.gov.pr.idr.infra.property_management.property.persistence.collaborator.PropertyCollaboratorJPAEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
    private BigDecimal nakedAveragePrice;
    private BigDecimal leaseAveragePrice;
    private Double dairyCattleFarming;
    private Double perennialPasture;
    private Double summerPlowing;
    private Double winterPlowing;

    @Column(name = "producer_id", nullable = false)
    private UUID producerId;

    @Column(name = "city_id", nullable = false)
    private UUID cityId;

    @ElementCollection
    @CollectionTable(name = "property_technician", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "user_id", nullable = false)
    private List<UUID> technicianIds;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "property_id", nullable = false)
    private List<PropertyCollaboratorJPAEntity> collaborators;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "property_id", nullable = false)
    private List<PropertyAttachmentJPAEntity> attachments;

    @Version
    private Long version;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public static PropertyJPAEntity fromDomain(final Property property) {
        return new PropertyJPAEntity(
                property.getId().id(),
                property.getName(),
                property.getCoord().latitude(),
                property.getCoord().longitude(),
                property.getNakedAveragePrice(),
                property.getLeaseAveragePrice(),
                property.getDairyCattleFarmingArea(),
                property.getPerennialPastureArea(),
                property.getSummerPlowingArea(),
                property.getWinterPlowingArea(),
                property.getProducerId().id(),
                property.getCityId().id(),
                property.getTechnicianIds().stream().map(UserID::id).toList(),
                property.getCollaborators().stream().map(PropertyCollaboratorJPAEntity::fromDomain).toList(),
                property.getAttachments().stream().map(PropertyAttachmentJPAEntity::fromDomain).toList(),
                property.getVersion(),
                property.getUpdatedAt()
        );
    }

    public Property toDomain() {
        final var property = Property.with(
                PropertyID.from(this.id),
                this.name,
                Coord.from(this.latitude, this.longitude),
                this.nakedAveragePrice,
                this.leaseAveragePrice,
                this.dairyCattleFarming,
                this.perennialPasture,
                this.summerPlowing,
                this.winterPlowing,
                ProducerID.from(this.producerId),
                CityID.from(this.cityId),
                this.technicianIds.stream().map(UserID::from).toList(),
                this.collaborators.stream()
                                  .map(PropertyCollaboratorJPAEntity::toDomain)
                                  .toList(),
                this.version,
                this.updatedAt
        );
        this.attachments.stream()
                .map(PropertyAttachmentJPAEntity::toDomain)
                .forEach(property::addAttachment);
        return property;
    }
}
