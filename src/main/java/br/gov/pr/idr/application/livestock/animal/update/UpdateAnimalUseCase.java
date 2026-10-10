package br.gov.pr.idr.application.livestock.animal.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

@CommandUseCase
public class UpdateAnimalUseCase extends UseCase<UpdateAnimalCommand, UpdateAnimalOutput> {

    private final AnimalGateway animalGateway;
    private final BreedGateway breedGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateAnimalUseCase(final AnimalGateway animalGateway, final BreedGateway breedGateway,
                                final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.breedGateway = breedGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateAnimalOutput execute(final UpdateAnimalCommand command) {
        final var existingAnimal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        final var notification = NotificationValidation.create();
        final var breedId = BreedID.from(command.breedId());
        if (!breedGateway.existsById(breedId)) {
            throw new NotificationException("Raça com o ID %s não encontrada".formatted(breedId.id()),
                    notification);
        }

        AnimalID animalMotherId = existingAnimal.getAnimalMotherId().orElse(null);
        if (command.animalMotherId() != null) {
            animalMotherId = AnimalID.from(command.animalMotherId());
            final var motherId = animalMotherId;
            if (motherId.equals(existingAnimal.getId())) {
                throw new NotificationException(
                        "Animal com o ID %s não pode ser sua própria mãe".formatted(motherId.id()), notification);
            }
            final var motherBelongsToProperty = animalGateway.findById(motherId)
                    .filter(mother -> mother.getPropertyId().equals(PropertyID.from(command.propertyId())))
                    .isPresent();
            if (!motherBelongsToProperty) {
                throw new NotificationException(
                        "Animal (mãe) com o ID %s não encontrado".formatted(motherId.id()), notification);
            }
        }

        // PATCH semantics: a field absent (null) in the request keeps its current value.
        final var updatedAnimal = existingAnimal.update(animalMotherId, breedId,
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

    private static <T> T orCurrent(final T incoming, final T current) {
        return incoming != null ? incoming : current;
    }
}
