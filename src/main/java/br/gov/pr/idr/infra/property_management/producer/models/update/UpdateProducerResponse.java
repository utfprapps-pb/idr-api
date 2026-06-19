package br.gov.pr.idr.infra.property_management.producer.models.update;

import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerOutput;

import java.util.UUID;

public record UpdateProducerResponse(UUID id, String name) {

    public static UpdateProducerResponse from(final UpdateProducerOutput output) {
        return new UpdateProducerResponse(output.id(), output.name());
    }
}
