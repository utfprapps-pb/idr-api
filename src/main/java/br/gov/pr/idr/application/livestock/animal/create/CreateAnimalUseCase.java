package br.gov.pr.idr.application.livestock.animal.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

@CommandUseCase
public class CreateAnimalUseCase extends UseCase<CreateAnimalCommand, CreateAnimalOutput> {

    private final AnimalGateway animalGateway;
    private final BreedGateway breedGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalUseCase(final AnimalGateway animalGateway, final BreedGateway breedGateway,
                                final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.breedGateway = breedGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalOutput execute(final CreateAnimalCommand command) {
        accessPolicy.ensureAccessToProperty(command.propertyId(), command.username());

        final var notification = NotificationValidation.create();
        final var breedId = BreedID.from(command.breedId());
        if (!breedGateway.existsById(breedId)) {
            throw new NotificationException("Raça com o ID %s não encontrada".formatted(breedId.id()),
                    notification);
        }

        AnimalID animalMotherId = null;
        if (command.animalMotherId() != null) {
            animalMotherId = AnimalID.from(command.animalMotherId());
            final var motherId = animalMotherId;
            if (!animalGateway.existsByIdAndPropertyId(motherId, PropertyID.from(command.propertyId()))) {
                throw new NotificationException(
                        "Animal (mãe) com o ID %s não encontrado".formatted(motherId.id()), notification);
            }
        }

        final var animal = Animal.create(PropertyID.from(command.propertyId()), animalMotherId, breedId,
                command.type(), command.identifier(), command.gender(), command.bornCondition(),
                command.bornDate(), command.bornWeight(), command.previousWeight(), command.currentWeight(),
                command.ecc());

        return CreateAnimalOutput.from(animalGateway.save(animal));
    }
}
