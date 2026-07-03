package br.gov.pr.idr.application.property_management.producer.retrieve.get;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@QueryUseCase
public class GetProducerByIdUseCase extends UseCase<UUID, GetProducerByIdOutput> {

    private final ProducerGateway producerGateway;

    public GetProducerByIdUseCase(final ProducerGateway producerGateway) {
        this.producerGateway = producerGateway;
    }

    @Override
    public GetProducerByIdOutput execute(final UUID id) {
        final var producerId = ProducerID.from(id);
        return producerGateway.findById(producerId)
                .map(GetProducerByIdOutput::from)
                .orElseThrow(() -> NotFoundException.with(Producer.class, producerId));
    }
}
