package br.gov.pr.idr.application.livestock.animal.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

import java.util.stream.Collectors;

@QueryUseCase
public class ListAnimalUseCase extends UseCase<ListAnimalCommand, Pagination<ListAnimalOutput>> {

    private final AnimalGateway animalGateway;
    private final LivestockNameLookup nameLookup;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalUseCase(final AnimalGateway animalGateway, final LivestockNameLookup nameLookup,
                             final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.nameLookup = nameLookup;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalOutput> execute(final ListAnimalCommand command) {
        accessPolicy.ensureAccessToProperty(command.propertyId(), command.username());
        final var page = animalGateway.search(PropertyID.from(command.propertyId()), command.query());

        final var breedNames = nameLookup.breedNames(
                page.items().stream().map(Animal::getBreedId).collect(Collectors.toSet()));

        return page.map(animal -> ListAnimalOutput.from(animal, breedNames.get(animal.getBreedId())));
    }
}
