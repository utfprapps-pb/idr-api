package br.gov.pr.idr.application.livestock.animal_childbirth.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;

@CommandUseCase
public class CreateAnimalChildbirthUseCase extends UseCase<CreateAnimalChildbirthCommand,
        CreateAnimalChildbirthOutput> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final LivestockReferencePolicy referencePolicy;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalChildbirthUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                         final LivestockReferencePolicy referencePolicy,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.referencePolicy = referencePolicy;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalChildbirthOutput execute(final CreateAnimalChildbirthCommand command) {
        final var mother = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());
        referencePolicy.ensureBreedExists(command.breedId());
        referencePolicy.ensureAnimalInProperty(command.calfId(), mother.getPropertyId(), "bezerro");

        final var animalChildbirth = AnimalChildbirth.create(mother.getId(), command.childbirthDate(),
                command.gender(), command.weight(), command.condition(),
                command.breedId() == null ? null : BreedID.from(command.breedId()),
                command.calfId() == null ? null : AnimalID.from(command.calfId()));

        return CreateAnimalChildbirthOutput.from(animalChildbirthGateway.save(animalChildbirth));
    }
}
