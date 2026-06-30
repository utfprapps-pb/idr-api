package br.gov.pr.idr.application.property_management.producer.retrieve.get;

import br.gov.pr.idr.domain.property_management.producer.Producer;

import java.time.Instant;
import java.util.UUID;

public record GetProducerByIdOutput(UUID id, String name, String cpf, Long version, Instant updatedAt) {

    public static GetProducerByIdOutput from(final Producer producer) {
        return new GetProducerByIdOutput(
                producer.getId().id(),
                producer.getName(),
                producer.getCpf().value(),
                producer.getVersion(),
                producer.getUpdatedAt()
        );
    }
}
