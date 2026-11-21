package br.gov.pr.idr.application.livestock.animal.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;

import java.util.Objects;

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
                command.scope());
        referencePolicy.ensureBreedExists(command.breedId());
        // A mãe só é revalidada quando muda: o vínculo já gravado com uma mãe excluída depois não pode
        // impedir a edição dos outros campos.
        final var currentMotherId = existingAnimal.getAnimalMotherId().map(AnimalID::id).orElse(null);
        if (!Objects.equals(command.animalMotherId(), currentMotherId)) {
            referencePolicy.ensureAnimalInProperty(command.animalMotherId(), PropertyID.from(command.propertyId()),
                    "mãe");
        }

        // Semântica de PUT: os campos de identificação são substituídos (nulo apaga); as medidas não fazem
        // parte da edição e são preservadas.
        final var updatedAnimal = existingAnimal.update(
                command.animalMotherId() == null ? null : AnimalID.from(command.animalMotherId()),
                BreedID.from(command.breedId()),
                command.type(),
                command.identifier(),
                command.gender(),
                command.bornCondition(),
                command.bornDate(),
                command.bornWeight(),
                existingAnimal.getPreviousWeight(),
                existingAnimal.getCurrentWeight(),
                existingAnimal.getEcc());

        return UpdateAnimalOutput.from(animalGateway.update(updatedAnimal));
    }
}
