package br.gov.pr.idr.application.livestock.animal.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;

@QueryUseCase
public class GetAnimalByIdUseCase extends UseCase<GetAnimalByIdCommand, GetAnimalByIdOutput> {

    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalByIdUseCase(final AnimalAccessPolicy accessPolicy) {
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalByIdOutput execute(final GetAnimalByIdCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());
        return GetAnimalByIdOutput.from(animal);
    }
}
