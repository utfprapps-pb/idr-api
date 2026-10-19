package br.gov.pr.idr.application.livestock.animal_purchases.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteAnimalPurchasesUseCase extends VoidUseCase<DeleteAnimalPurchasesCommand> {

    private final AnimalPurchasesGateway animalPurchasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteAnimalPurchasesUseCase(final AnimalPurchasesGateway animalPurchasesGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalPurchasesGateway = animalPurchasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteAnimalPurchasesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalPurchasesID.from(command.animalPurchasesId());
        final var animalPurchases = animalPurchasesGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalPurchases.class, id));

        animalPurchasesGateway.deleteById(animalPurchases.getId());
    }
}
