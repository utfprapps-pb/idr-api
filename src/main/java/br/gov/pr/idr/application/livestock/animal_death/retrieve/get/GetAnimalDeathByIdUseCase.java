package br.gov.pr.idr.application.livestock.animal_death.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetAnimalDeathByIdUseCase extends UseCase<GetAnimalDeathByIdCommand,
        GetAnimalDeathByIdOutput> {

    private final AnimalDeathGateway animalDeathGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalDeathByIdUseCase(final AnimalDeathGateway animalDeathGateway,
                                     final AnimalAccessPolicy accessPolicy) {
        this.animalDeathGateway = animalDeathGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalDeathByIdOutput execute(final GetAnimalDeathByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalDeathID.from(command.animalDeathId());
        final var animalDeath = animalDeathGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(AnimalDeath.class, id));

        return GetAnimalDeathByIdOutput.from(animalDeath);
    }
}
