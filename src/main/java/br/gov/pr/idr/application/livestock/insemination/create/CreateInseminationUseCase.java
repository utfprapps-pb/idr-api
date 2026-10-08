package br.gov.pr.idr.application.livestock.insemination.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;

@CommandUseCase
public class CreateInseminationUseCase extends UseCase<CreateInseminationCommand, CreateInseminationOutput> {

    private final InseminationGateway inseminationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateInseminationUseCase(final InseminationGateway inseminationGateway,
                                      final AnimalAccessPolicy accessPolicy) {
        this.inseminationGateway = inseminationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateInseminationOutput execute(final CreateInseminationCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        final var insemination = Insemination.create(animal.getId(), command.iaDate(), command.bull());

        return CreateInseminationOutput.from(inseminationGateway.save(insemination));
    }
}
