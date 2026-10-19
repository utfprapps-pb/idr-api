package br.gov.pr.idr.application.livestock.animal.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;

@CommandUseCase
public class CreateAnimalUseCase extends UseCase<CreateAnimalCommand, CreateAnimalOutput> {

    private final AnimalGateway animalGateway;
    private final LivestockReferencePolicy referencePolicy;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalUseCase(final AnimalGateway animalGateway, final LivestockReferencePolicy referencePolicy,
                                final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.referencePolicy = referencePolicy;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalOutput execute(final CreateAnimalCommand command) {
        accessPolicy.ensureAccessToProperty(command.propertyId(), command.username());
        final var propertyId = PropertyID.from(command.propertyId());
        referencePolicy.ensureBreedExists(command.breedId());
        referencePolicy.ensureAnimalInProperty(command.animalMotherId(), propertyId, "mãe");

        final var animal = Animal.create(propertyId,
                command.animalMotherId() == null ? null : AnimalID.from(command.animalMotherId()),
                BreedID.from(command.breedId()), command.type(), command.identifier(), command.gender(),
                command.bornCondition(), command.bornDate(), command.bornWeight(), command.previousWeight(),
                command.currentWeight(), command.ecc());

        return CreateAnimalOutput.from(animalGateway.save(animal));
    }
}
