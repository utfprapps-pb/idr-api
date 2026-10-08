package br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetAnimalPurchasesByIdUseCase extends UseCase<GetAnimalPurchasesByIdCommand,
        GetAnimalPurchasesByIdOutput> {

    private final AnimalPurchasesGateway animalPurchasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalPurchasesByIdUseCase(final AnimalPurchasesGateway animalPurchasesGateway,
                                          final AnimalAccessPolicy accessPolicy) {
        this.animalPurchasesGateway = animalPurchasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalPurchasesByIdOutput execute(final GetAnimalPurchasesByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalPurchasesID.from(command.animalPurchasesId());
        final var animalPurchases = animalPurchasesGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(AnimalPurchases.class, id));

        return GetAnimalPurchasesByIdOutput.from(animalPurchases);
    }
}
