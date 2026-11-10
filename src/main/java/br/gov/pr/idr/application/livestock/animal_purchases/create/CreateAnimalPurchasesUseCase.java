package br.gov.pr.idr.application.livestock.animal_purchases.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;

@CommandUseCase
public class CreateAnimalPurchasesUseCase extends UseCase<CreateAnimalPurchasesCommand,
        CreateAnimalPurchasesOutput> {

    private final AnimalPurchasesGateway animalPurchasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalPurchasesUseCase(final AnimalPurchasesGateway animalPurchasesGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalPurchasesGateway = animalPurchasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalPurchasesOutput execute(final CreateAnimalPurchasesCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.scope());

        final var animalPurchases = AnimalPurchases.create(animal.getId(), command.datePurchase(),
                command.birthDate(), command.value(), command.weight());

        return CreateAnimalPurchasesOutput.from(animalPurchasesGateway.save(animalPurchases));
    }
}
