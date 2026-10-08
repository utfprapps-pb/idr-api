package br.gov.pr.idr.application.livestock.animal_sales.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;

@CommandUseCase
public class CreateAnimalSalesUseCase extends UseCase<CreateAnimalSalesCommand,
        CreateAnimalSalesOutput> {

    private final AnimalSalesGateway animalSalesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalSalesUseCase(final AnimalSalesGateway animalSalesGateway,
                                     final AnimalAccessPolicy accessPolicy) {
        this.animalSalesGateway = animalSalesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalSalesOutput execute(final CreateAnimalSalesCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        final var animalSales = AnimalSales.create(animal.getId(), command.dateSale(),
                command.reason(), command.value(), command.destination());

        return CreateAnimalSalesOutput.from(animalSalesGateway.save(animalSales));
    }
}
