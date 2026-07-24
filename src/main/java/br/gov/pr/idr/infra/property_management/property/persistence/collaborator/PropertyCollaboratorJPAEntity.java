package br.gov.pr.idr.infra.property_management.property.persistence.collaborator;

import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaboratorID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "PropertyCollaborator")
@Table(name = "property_collaborator")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PropertyCollaboratorJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "hours_per_day", nullable = false)
    private String hoursPerDay;

    public static PropertyCollaboratorJPAEntity fromDomain(final PropertyCollaborator collaborator) {
        return new PropertyCollaboratorJPAEntity(
                collaborator.getId().id(),
                collaborator.getName(),
                collaborator.getHoursPerDay()
        );
    }

    public PropertyCollaborator toDomain() {
        return PropertyCollaborator.with(
                PropertyCollaboratorID.from(this.id),
                this.name,
                this.hoursPerDay
        );
    }
}
