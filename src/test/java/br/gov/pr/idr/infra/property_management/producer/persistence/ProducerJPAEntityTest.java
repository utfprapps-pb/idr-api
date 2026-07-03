package br.gov.pr.idr.infra.property_management.producer.persistence;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ProducerJPAEntity")
class ProducerJPAEntityTest {

    @Test
    @DisplayName("deve converter entidade JPA em agregado de domínio")
    void shouldConvertToDomain() {
        final var producer = Producer.create("João da Silva", CPF.from("529.982.247-25"));
        final var entity = ProducerJPAEntity.fromDomain(producer);

        final var domain = entity.toDomain();

        assertEquals(producer.getId(), domain.getId());
        assertEquals(producer.getName(), domain.getName());
        assertEquals(producer.getCpf(), domain.getCpf());
        assertEquals(producer.getVersion(), domain.getVersion());
        assertEquals(producer.getUpdatedAt(), domain.getUpdatedAt());
    }
}
