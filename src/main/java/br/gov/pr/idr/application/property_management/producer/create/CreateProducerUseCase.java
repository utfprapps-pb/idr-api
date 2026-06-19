package br.gov.pr.idr.application.property_management.producer.create;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.shared.exceptions.DomainException;

@CommandUseCase
public class CreateProducerUseCase extends UseCase<CreateProducerCommand, CreateProducerOutput> {

    private final ProducerGateway producerGateway;

    public CreateProducerUseCase(final ProducerGateway producerGateway) {
        this.producerGateway = producerGateway;
    }

    @Override
    public CreateProducerOutput execute(final CreateProducerCommand command) {
        final var cpf = CPF.from(command.cpf());
        if (producerGateway.existsByCpf(cpf)) {
            throw DomainException.from("CPF já cadastrado para outro produtor");
        }
        final var producer = Producer.create(command.name(), cpf);
        return CreateProducerOutput.from(producerGateway.save(producer));
    }
}
