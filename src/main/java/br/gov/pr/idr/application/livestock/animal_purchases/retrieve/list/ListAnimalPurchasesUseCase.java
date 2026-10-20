package br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListAnimalPurchasesUseCase extends UseCase<ListAnimalPurchasesCommand,
        Pagination<ListAnimalPurchasesOutput>> {

    private final AnimalPurchasesGateway animalPurchasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalPurchasesUseCase(final AnimalPurchasesGateway animalPurchasesGateway,
                                       final AnimalAccessPolicy accessPolicy) {
        this.animalPurchasesGateway = animalPurchasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalPurchasesOutput> execute(final ListAnimalPurchasesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        return animalPurchasesGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListAnimalPurchasesOutput::from);
    }
}
