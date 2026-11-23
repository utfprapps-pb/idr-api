package br.gov.pr.idr.application.livestock.animalsize.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateAnimalSizeUseCase extends UseCase<UpdateAnimalSizeCommand, UpdateAnimalSizeOutput> {

    private final AnimalSizeGateway animalSizeGateway;

    public UpdateAnimalSizeUseCase(AnimalSizeGateway animalSizeGateway) {
        this.animalSizeGateway = animalSizeGateway;
    }

    @Override
    public UpdateAnimalSizeOutput execute(UpdateAnimalSizeCommand command) {
        final var id = AnimalSizeID.from(command.id());
        final var sizeName = command.sizeName();

        final var animalSize = animalSizeGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(
                        "AnimalSize com o ID %s não encontrada".formatted(id.id())))
                .update(sizeName);

        return UpdateAnimalSizeOutput.from(animalSizeGateway.update(animalSize));
    }
}
