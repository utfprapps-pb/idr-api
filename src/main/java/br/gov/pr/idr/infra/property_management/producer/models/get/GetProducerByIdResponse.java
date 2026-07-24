package br.gov.pr.idr.infra.property_management.producer.models.get;

import br.gov.pr.idr.application.property_management.producer.retrieve.get.GetProducerByIdOutput;

import java.time.Instant;
import java.util.UUID;

public record GetProducerByIdResponse(
        UUID id,
        String name,
        String cpf,
        Long version,
        Instant updatedAt
) {

    public static GetProducerByIdResponse from(final GetProducerByIdOutput output) {
        return new GetProducerByIdResponse(output.id(),
                                           output.name(),
                                           output.cpf(),
                                           output.version(),
                                           output.updatedAt()
        );
    }
}
