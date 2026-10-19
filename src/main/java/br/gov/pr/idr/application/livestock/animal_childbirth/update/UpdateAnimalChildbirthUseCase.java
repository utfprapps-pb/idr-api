package br.gov.pr.idr.application.livestock.animal_childbirth.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateAnimalChildbirthUseCase extends UseCase<UpdateAnimalChildbirthCommand,
        UpdateAnimalChildbirthOutput> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final LivestockReferencePolicy referencePolicy;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateAnimalChildbirthUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                         final LivestockReferencePolicy referencePolicy,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.referencePolicy = referencePolicy;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateAnimalChildbirthOutput execute(final UpdateAnimalChildbirthCommand command) {
        final var mother = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());
        final var id = AnimalChildbirthID.from(command.animalChildbirthId());
        final var animalChildbirth = animalChildbirthGateway.findByIdAndAnimalId(id, mother.getId())
                .orElseThrow(() -> NotFoundException.with(AnimalChildbirth.class, id));
        referencePolicy.ensureBreedExists(command.breedId());
        referencePolicy.ensureAnimalInProperty(command.calfId(), mother.getPropertyId(), "bezerro");

        final var updatedAnimalChildbirth = animalChildbirth.update(command.childbirthDate(), command.gender(),
                command.weight(), command.condition(),
                command.breedId() == null ? null : BreedID.from(command.breedId()),
                command.calfId() == null ? null : AnimalID.from(command.calfId()));

        return UpdateAnimalChildbirthOutput.from(animalChildbirthGateway.update(updatedAnimalChildbirth));
    }
}
