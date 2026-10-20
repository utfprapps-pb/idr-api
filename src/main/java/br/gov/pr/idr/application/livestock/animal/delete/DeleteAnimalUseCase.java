package br.gov.pr.idr.application.livestock.animal.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;

@CommandUseCase
public class DeleteAnimalUseCase extends VoidUseCase<DeleteAnimalCommand> {

    private final AnimalGateway animalGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteAnimalUseCase(final AnimalGateway animalGateway, final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteAnimalCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.scope());
        animalGateway.deleteById(animal.getId());
    }
}
