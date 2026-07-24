package br.gov.pr.idr.infra.property_management.producer.models.create;

import br.gov.pr.idr.application.property_management.producer.create.CreateProducerOutput;

import java.util.UUID;

public record CreateProducerResponse(UUID id, String name) {

    public static CreateProducerResponse from(final CreateProducerOutput output) {
        return new CreateProducerResponse(output.id(), output.name());
    }
}
