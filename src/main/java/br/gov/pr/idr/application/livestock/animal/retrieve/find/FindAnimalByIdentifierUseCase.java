package br.gov.pr.idr.application.livestock.animal.retrieve.find;

import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdOutput;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;

import java.util.Optional;

@QueryUseCase
public class FindAnimalByIdentifierUseCase
        extends UseCase<FindAnimalByIdentifierCommand, Optional<GetAnimalByIdOutput>> {

    private final AnimalGateway animalGateway;
    private final BreedGateway breedGateway;
    private final AnimalAccessPolicy accessPolicy;

    public FindAnimalByIdentifierUseCase(final AnimalGateway animalGateway, final BreedGateway breedGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.breedGateway = breedGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Optional<GetAnimalByIdOutput> execute(final FindAnimalByIdentifierCommand command) {
        accessPolicy.ensureAccessToProperty(command.propertyId(), command.username());
        return animalGateway.findByPropertyIdAndIdentifier(PropertyID.from(command.propertyId()),
                command.identifier()).map(animal -> GetAnimalByIdOutput.from(animal,
                breedGateway.findById(animal.getBreedId()).map(Breed::getBreedName).orElse(null)));
    }
}
