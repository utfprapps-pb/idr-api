package br.gov.pr.idr.application.livestock.animal_death.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

@CommandUseCase
public class CreateAnimalDeathUseCase extends UseCase<CreateAnimalDeathCommand,
        CreateAnimalDeathOutput> {

    private final AnimalDeathGateway animalDeathGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalDeathUseCase(final AnimalDeathGateway animalDeathGateway,
                                    final AnimalAccessPolicy accessPolicy) {
        this.animalDeathGateway = animalDeathGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalDeathOutput execute(final CreateAnimalDeathCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        // An animal dies only once: an existing death is edited, never duplicated.
        if (animalDeathGateway.existsByAnimalId(animal.getId())) {
            throw new NotificationException("O animal com o ID %s já possui morte registrada"
                    .formatted(animal.getId().id()), NotificationValidation.create());
        }

        final var animalDeath = AnimalDeath.create(animal.getId(), command.reason(),
                command.deathDate());

        return CreateAnimalDeathOutput.from(animalDeathGateway.save(animalDeath));
    }
}
