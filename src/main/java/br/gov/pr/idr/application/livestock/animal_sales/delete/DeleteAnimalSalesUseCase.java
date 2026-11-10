package br.gov.pr.idr.application.livestock.animal_sales.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteAnimalSalesUseCase extends VoidUseCase<DeleteAnimalSalesCommand> {

    private final AnimalSalesGateway animalSalesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteAnimalSalesUseCase(final AnimalSalesGateway animalSalesGateway,
                                     final AnimalAccessPolicy accessPolicy) {
        this.animalSalesGateway = animalSalesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteAnimalSalesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = AnimalSalesID.from(command.animalSalesId());
        final var animalSales = animalSalesGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalSales.class, id));

        animalSalesGateway.deleteById(animalSales.getId());
    }
}
