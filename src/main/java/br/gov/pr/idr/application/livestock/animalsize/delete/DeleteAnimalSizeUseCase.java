package br.gov.pr.idr.application.livestock.animalsize.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeleteAnimalSizeUseCase extends VoidUseCase<UUID> {

    private final AnimalSizeGateway animalSizeGateway;

    public DeleteAnimalSizeUseCase(AnimalSizeGateway animalSizeGateway) {
        this.animalSizeGateway = animalSizeGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var animalSizeId = AnimalSizeID.from(id);
        if (!animalSizeGateway.existsById(animalSizeId)) {
            throw NotFoundException.with(AnimalSize.class, animalSizeId);
        }
        animalSizeGateway.deleteById(animalSizeId);
    }
}
