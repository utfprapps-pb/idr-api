package br.gov.pr.idr.infra.property_management.producer.persistence;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.infra.shared.persistence.SyncableJPAEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity(name = "Producer")
@Table(name = "producer")
@SQLRestriction("deleted_at is null")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProducerJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cpf;

    public static ProducerJPAEntity fromDomain(final Producer producer) {
        final var entity = new ProducerJPAEntity(
                producer.getId().id(),
                producer.getName(),
                producer.getCpf().value()
        );
        entity.applyVersion(producer.getVersion());
        return entity;
    }

    public Producer toDomain() {
        return Producer.with(ProducerID.from(this.id),
                             this.name,
                             CPF.from(this.cpf),
                             this.getVersion(),
                             this.getUpdatedAt());
    }
}
