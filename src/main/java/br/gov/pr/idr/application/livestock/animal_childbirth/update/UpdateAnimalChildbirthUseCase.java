package br.gov.pr.idr.application.livestock.animal_childbirth.update;

import br.gov.pr.idr.application.livestock.animal_childbirth.ChildbirthReferencePolicy;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateAnimalChildbirthUseCase extends UseCase<UpdateAnimalChildbirthCommand,
        UpdateAnimalChildbirthOutput> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final BreedGateway breedGateway;
    private final AnimalGateway animalGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateAnimalChildbirthUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                         final BreedGateway breedGateway, final AnimalGateway animalGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.breedGateway = breedGateway;
        this.animalGateway = animalGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateAnimalChildbirthOutput execute(final UpdateAnimalChildbirthCommand command) {
        final var mother = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());
        final var id = AnimalChildbirthID.from(command.animalChildbirthId());
        final var animalChildbirth = animalChildbirthGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(mother.getId()))
                .orElseThrow(() -> NotFoundException.with(AnimalChildbirth.class, id));
        ChildbirthReferencePolicy.ensureValid(breedGateway, animalGateway, mother, command.breedId(), command.calfId());

        final var updatedAnimalChildbirth = animalChildbirth.update(command.childbirthDate(), command.gender(),
                command.weight(), command.condition(),
                command.breedId() == null ? null : BreedID.from(command.breedId()),
                command.calfId() == null ? null : AnimalID.from(command.calfId()));

        return UpdateAnimalChildbirthOutput.from(animalChildbirthGateway.update(updatedAnimalChildbirth));
    }
}
