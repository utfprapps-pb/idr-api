package br.gov.pr.idr.application.livestock.animal_sales.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetAnimalSalesByIdUseCase extends UseCase<GetAnimalSalesByIdCommand,
        GetAnimalSalesByIdOutput> {

    private final AnimalSalesGateway animalSalesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalSalesByIdUseCase(final AnimalSalesGateway animalSalesGateway,
                                      final AnimalAccessPolicy accessPolicy) {
        this.animalSalesGateway = animalSalesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalSalesByIdOutput execute(final GetAnimalSalesByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = AnimalSalesID.from(command.animalSalesId());
        final var animalSales = animalSalesGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalSales.class, id));

        return GetAnimalSalesByIdOutput.from(animalSales);
    }
}
