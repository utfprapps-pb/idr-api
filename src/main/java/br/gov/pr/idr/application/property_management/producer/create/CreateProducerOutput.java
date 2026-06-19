package br.gov.pr.idr.application.property_management.producer.create;

import br.gov.pr.idr.domain.property_management.producer.Producer;

import java.util.UUID;

public record CreateProducerOutput(UUID id, String name) {

    public static CreateProducerOutput from(final Producer producer) {
        return new CreateProducerOutput(producer.getId().id(), producer.getName());
    }
}
