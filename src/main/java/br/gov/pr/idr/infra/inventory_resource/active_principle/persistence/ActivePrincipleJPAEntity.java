package br.gov.pr.idr.infra.inventory_resource.active_principle.persistence;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
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
@Table(name = "inventory_resource_active_principle")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivePrincipleJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    public static ActivePrincipleJPAEntity from(final ActivePrinciple principle) {
        return new ActivePrincipleJPAEntity(
                principle.getId().id(),
                principle.getName()
        );
    }

    public ActivePrinciple toAggregate() {
        return ActivePrinciple.with(
                ActivePrincipleID.from(this.id),
                this.name
        );
    }
}
