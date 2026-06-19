package br.gov.pr.idr.application.property_management.producer.update;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.shared.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;

@CommandUseCase
public class UpdateProducerUseCase extends UseCase<UpdateProducerCommand, UpdateProducerOutput> {

    private final ProducerGateway producerGateway;

    public UpdateProducerUseCase(final ProducerGateway producerGateway) {
        this.producerGateway = producerGateway;
    }

    @Override
    public UpdateProducerOutput execute(final UpdateProducerCommand command) {
        final var producerId = ProducerID.from(command.id());
        final var producer = producerGateway.findById(producerId)
                .orElseThrow(() -> NotFoundException.with(Producer.class, producerId));

        final var newCpf = CPF.from(command.cpf());
        if (!producer.getCpf().equals(newCpf) && producerGateway.existsByCpf(newCpf)) {
            throw DomainException.from("CPF já cadastrado para outro produtor");
        }

        return UpdateProducerOutput.from(
                producerGateway.update(producer.update(command.name(), newCpf))
        );
    }
}
