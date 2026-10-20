package br.gov.pr.idr.application.livestock.animal.retrieve.find;

import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdOutput;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;

import java.util.Optional;

@QueryUseCase
public class FindAnimalByIdentifierUseCase
        extends UseCase<FindAnimalByIdentifierCommand, Optional<GetAnimalByIdOutput>> {

    private final AnimalGateway animalGateway;
    private final LivestockNameLookup nameLookup;
    private final AnimalAccessPolicy accessPolicy;

    public FindAnimalByIdentifierUseCase(final AnimalGateway animalGateway, final LivestockNameLookup nameLookup,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.nameLookup = nameLookup;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Optional<GetAnimalByIdOutput> execute(final FindAnimalByIdentifierCommand command) {
        accessPolicy.ensureAccessToProperty(command.propertyId(), command.scope());
        return animalGateway.findByPropertyIdAndIdentifier(PropertyID.from(command.propertyId()),
                command.identifier()).map(animal -> GetAnimalByIdOutput.from(animal,
                nameLookup.breedName(animal.getBreedId())));
    }
}
