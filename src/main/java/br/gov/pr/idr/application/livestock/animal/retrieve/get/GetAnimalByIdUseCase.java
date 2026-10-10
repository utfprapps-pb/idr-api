package br.gov.pr.idr.application.livestock.animal.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;

@QueryUseCase
public class GetAnimalByIdUseCase extends UseCase<GetAnimalByIdCommand, GetAnimalByIdOutput> {

    private final AnimalAccessPolicy accessPolicy;
    private final BreedGateway breedGateway;

    public GetAnimalByIdUseCase(final AnimalAccessPolicy accessPolicy, final BreedGateway breedGateway) {
        this.accessPolicy = accessPolicy;
        this.breedGateway = breedGateway;
    }

    @Override
    public GetAnimalByIdOutput execute(final GetAnimalByIdCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());
        final var breedName = breedGateway.findById(animal.getBreedId()).map(Breed::getBreedName).orElse(null);
        return GetAnimalByIdOutput.from(animal, breedName);
    }
}
