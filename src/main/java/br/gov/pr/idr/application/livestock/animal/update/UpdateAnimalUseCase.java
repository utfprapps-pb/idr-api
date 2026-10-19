package br.gov.pr.idr.application.livestock.animal.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;

@CommandUseCase
public class UpdateAnimalUseCase extends UseCase<UpdateAnimalCommand, UpdateAnimalOutput> {

    private final AnimalGateway animalGateway;
    private final LivestockReferencePolicy referencePolicy;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateAnimalUseCase(final AnimalGateway animalGateway, final LivestockReferencePolicy referencePolicy,
                                final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.referencePolicy = referencePolicy;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateAnimalOutput execute(final UpdateAnimalCommand command) {
        final var existingAnimal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());
        referencePolicy.ensureBreedExists(command.breedId());
        referencePolicy.ensureAnimalInProperty(command.animalMotherId(), PropertyID.from(command.propertyId()),
                "mãe");

        final var updatedAnimal = existingAnimal.update(
                orCurrent(command.animalMotherId() == null ? null : AnimalID.from(command.animalMotherId()),
                        existingAnimal.getAnimalMotherId().orElse(null)),
                BreedID.from(command.breedId()),
                orCurrent(command.type(), existingAnimal.getType()),
                command.identifier(),
                orCurrent(command.gender(), existingAnimal.getGender()),
                orCurrent(command.bornCondition(), existingAnimal.getBornCondition()),
                orCurrent(command.bornDate(), existingAnimal.getBornDate()),
                orCurrent(command.bornWeight(), existingAnimal.getBornWeight()),
                orCurrent(command.previousWeight(), existingAnimal.getPreviousWeight()),
                orCurrent(command.currentWeight(), existingAnimal.getCurrentWeight()),
                orCurrent(command.ecc(), existingAnimal.getEcc()));

        return UpdateAnimalOutput.from(animalGateway.update(updatedAnimal));
    }

    /** Semântica de PATCH: campo ausente (null) na requisição mantém o valor atual. */
    private static <T> T orCurrent(final T incoming, final T current) {
        return incoming != null ? incoming : current;
    }
}
