package br.gov.pr.idr.infra.property_management.producer.models.search;

import br.gov.pr.idr.application.property_management.producer.retrieve.search.SearchProducerOutput;

import java.util.UUID;

public record SearchProducerResponse(UUID id, String name) {

    public static SearchProducerResponse from(final SearchProducerOutput output) {
        return new SearchProducerResponse(output.id(), output.name());
    }
}
