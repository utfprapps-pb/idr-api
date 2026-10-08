package br.gov.pr.idr.application.livestock.animal.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListAnimalUseCase extends UseCase<ListAnimalCommand, Pagination<ListAnimalOutput>> {

    private final AnimalGateway animalGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalUseCase(final AnimalGateway animalGateway, final AnimalAccessPolicy accessPolicy) {
        this.animalGateway = animalGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalOutput> execute(final ListAnimalCommand command) {
        accessPolicy.ensureAccessToProperty(command.propertyId(), command.username());
        return animalGateway.search(PropertyID.from(command.propertyId()), command.query())
                .map(ListAnimalOutput::from);
    }
}
