package br.gov.pr.idr.application.livestock.breed.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;

@CommandUseCase
public class CreateBreedUseCase extends UseCase<CreateBreedCommand, CreateBreedOutput> {

    private final BreedGateway breedGateway;

    public CreateBreedUseCase(BreedGateway breedGateway) {
        this.breedGateway = breedGateway;
    }

    @Override
    public CreateBreedOutput execute(CreateBreedCommand command) {
        final var breed = Breed.create(command.breedName());
        return CreateBreedOutput.from(breedGateway.save(breed));
    }
}
