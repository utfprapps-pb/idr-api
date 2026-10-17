package br.gov.pr.idr.application.livestock.breed.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateBreedUseCase extends UseCase<UpdateBreedCommand, UpdateBreedOutput> {

    private final BreedGateway breedGateway;

    public UpdateBreedUseCase(BreedGateway breedGateway) {
        this.breedGateway = breedGateway;
    }

    @Override
    public UpdateBreedOutput execute(UpdateBreedCommand command) {
        final var id = BreedID.from(command.id());
        final var breedName = command.breedName();

        final var breed = breedGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(
                        "Breed com o ID %s não encontrada".formatted(id.id())))
                .update(breedName);

        return UpdateBreedOutput.from(breedGateway.update(breed));
    }
}
