package br.gov.pr.idr.application.property_management.producer.retrieve.get;

import br.gov.pr.idr.domain.property_management.producer.Producer;

import java.util.UUID;

public record GetProducerByIdOutput(UUID id, String name, String cpf) {

    public static GetProducerByIdOutput from(final Producer producer) {
        return new GetProducerByIdOutput(
                producer.getId().id(),
                producer.getName(),
                producer.getCpf().value()
        );
    }
}
