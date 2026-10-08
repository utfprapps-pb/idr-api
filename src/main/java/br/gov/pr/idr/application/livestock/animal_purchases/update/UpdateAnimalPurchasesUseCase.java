package br.gov.pr.idr.application.livestock.animal_purchases.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateAnimalPurchasesUseCase extends UseCase<UpdateAnimalPurchasesCommand,
        UpdateAnimalPurchasesOutput> {

    private final AnimalPurchasesGateway animalPurchasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateAnimalPurchasesUseCase(final AnimalPurchasesGateway animalPurchasesGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalPurchasesGateway = animalPurchasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateAnimalPurchasesOutput execute(final UpdateAnimalPurchasesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalPurchasesID.from(command.animalPurchasesId());
        final var animalPurchases = animalPurchasesGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(AnimalPurchases.class, id));

        final var updatedAnimalPurchases = animalPurchases.update(command.datePurchase(), command.birthDate(),
                command.value());

        return UpdateAnimalPurchasesOutput.from(animalPurchasesGateway.update(updatedAnimalPurchases));
    }
}
