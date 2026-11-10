package br.gov.pr.idr.application.livestock.animal.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;

@QueryUseCase
public class GetAnimalByIdUseCase extends UseCase<GetAnimalByIdCommand, GetAnimalByIdOutput> {

    private final AnimalAccessPolicy accessPolicy;
    private final LivestockNameLookup nameLookup;

    public GetAnimalByIdUseCase(final AnimalAccessPolicy accessPolicy, final LivestockNameLookup nameLookup) {
        this.accessPolicy = accessPolicy;
        this.nameLookup = nameLookup;
    }

    @Override
    public GetAnimalByIdOutput execute(final GetAnimalByIdCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.scope());
        return GetAnimalByIdOutput.from(animal, nameLookup.breedName(animal.getBreedId()));
    }
}
