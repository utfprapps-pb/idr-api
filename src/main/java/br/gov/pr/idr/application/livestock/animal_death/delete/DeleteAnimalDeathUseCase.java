package br.gov.pr.idr.application.livestock.animal_death.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteAnimalDeathUseCase extends VoidUseCase<DeleteAnimalDeathCommand> {

    private final AnimalDeathGateway animalDeathGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteAnimalDeathUseCase(final AnimalDeathGateway animalDeathGateway,
                                    final AnimalAccessPolicy accessPolicy) {
        this.animalDeathGateway = animalDeathGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteAnimalDeathCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalDeathID.from(command.animalDeathId());
        final var animalDeath = animalDeathGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalDeath.class, id));

        animalDeathGateway.deleteById(animalDeath.getId());
    }
}
