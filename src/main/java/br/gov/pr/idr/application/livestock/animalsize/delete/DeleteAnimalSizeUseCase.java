package br.gov.pr.idr.application.livestock.animalsize.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;

import java.util.UUID;

@CommandUseCase
public class DeleteAnimalSizeUseCase extends VoidUseCase<UUID> {

    private final AnimalSizeGateway animalSizeGateway;
    private final AnimalGateway animalGateway;

    public DeleteAnimalSizeUseCase(AnimalSizeGateway animalSizeGateway, AnimalGateway animalGateway) {
        this.animalSizeGateway = animalSizeGateway;
        this.animalGateway = animalGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var animalSizeId = AnimalSizeID.from(id);
        if (!animalSizeGateway.existsById(animalSizeId)) {
            throw NotFoundException.with(AnimalSize.class, animalSizeId);
        }
        if (animalGateway.existsBySizeId(animalSizeId)) {
            throw new UnprocessableEntityException("Porte em uso por animais cadastrados não pode ser excluído");
        }
        animalSizeGateway.deleteById(animalSizeId);
    }
}
