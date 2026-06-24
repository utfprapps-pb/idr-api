package br.gov.pr.idr.application.property_management.producer.retrieve.search;

import br.gov.pr.idr.domain.property_management.producer.Producer;

import java.util.UUID;

public record SearchProducerOutput(UUID id, String name) {

    public static SearchProducerOutput from(final Producer producer) {
        return new SearchProducerOutput(
                producer.getId().id(),
                producer.getName()
        );
    }
}
