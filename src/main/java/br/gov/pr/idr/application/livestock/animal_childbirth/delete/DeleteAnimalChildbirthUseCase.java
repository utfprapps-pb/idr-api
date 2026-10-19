package br.gov.pr.idr.application.livestock.animal_childbirth.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteAnimalChildbirthUseCase extends VoidUseCase<DeleteAnimalChildbirthCommand> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteAnimalChildbirthUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteAnimalChildbirthCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalChildbirthID.from(command.animalChildbirthId());
        final var animalChildbirth = animalChildbirthGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalChildbirth.class, id));

        animalChildbirthGateway.deleteById(animalChildbirth.getId());
    }
}
