package br.gov.pr.idr.infra.property_management.producer.persistence;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "Producer")
@Table(name = "producer")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProducerJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Version
    private Long version;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public static ProducerJPAEntity fromDomain(final Producer producer) {
        return new ProducerJPAEntity(
                producer.getId().id(),
                producer.getName(),
                producer.getCpf().value(),
                producer.getVersion(),
                producer.getUpdatedAt()
        );
    }

    public Producer toDomain() {
        return Producer.with(ProducerID.from(this.id), this.name, CPF.from(this.cpf),
                this.version, this.updatedAt);
    }
}
