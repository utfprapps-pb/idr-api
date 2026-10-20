package br.gov.pr.idr.application.livestock.breed.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeleteBreedUseCase extends VoidUseCase<UUID> {

    private final BreedGateway breedGateway;

    public DeleteBreedUseCase(BreedGateway breedGateway) {
        this.breedGateway = breedGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var breedId = BreedID.from(id);
        if (!breedGateway.existsById(breedId)) {
            throw NotFoundException.with(Breed.class, breedId);
        }
        breedGateway.deleteById(breedId);
    }
}
