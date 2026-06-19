package br.gov.pr.idr.application.property_management.producer.update;

import br.gov.pr.idr.domain.property_management.producer.Producer;

import java.util.UUID;

public record UpdateProducerOutput(UUID id, String name) {

    public static UpdateProducerOutput from(final Producer producer) {
        return new UpdateProducerOutput(producer.getId().id(), producer.getName());
    }
}
