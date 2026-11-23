package br.gov.pr.idr.application.livestock.animalsize.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;

@CommandUseCase
public class CreateAnimalSizeUseCase extends UseCase<CreateAnimalSizeCommand, CreateAnimalSizeOutput> {

    private final AnimalSizeGateway animalSizeGateway;

    public CreateAnimalSizeUseCase(AnimalSizeGateway animalSizeGateway) {
        this.animalSizeGateway = animalSizeGateway;
    }

    @Override
    public CreateAnimalSizeOutput execute(CreateAnimalSizeCommand command) {
        final var animalSize = AnimalSize.create(command.sizeName());
        return CreateAnimalSizeOutput.from(animalSizeGateway.save(animalSize));
    }
}
