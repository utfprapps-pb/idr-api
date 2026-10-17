package br.gov.pr.idr.application.livestock.animal_sales.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListAnimalSalesUseCase extends UseCase<ListAnimalSalesCommand,
        Pagination<ListAnimalSalesOutput>> {

    private final AnimalSalesGateway animalSalesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalSalesUseCase(final AnimalSalesGateway animalSalesGateway,
                                   final AnimalAccessPolicy accessPolicy) {
        this.animalSalesGateway = animalSalesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalSalesOutput> execute(final ListAnimalSalesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        return animalSalesGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListAnimalSalesOutput::from);
    }
}
