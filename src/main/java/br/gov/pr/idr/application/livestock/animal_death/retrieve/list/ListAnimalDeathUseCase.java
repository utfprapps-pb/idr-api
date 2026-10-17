package br.gov.pr.idr.application.livestock.animal_death.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListAnimalDeathUseCase extends UseCase<ListAnimalDeathCommand,
        Pagination<ListAnimalDeathOutput>> {

    private final AnimalDeathGateway animalDeathGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalDeathUseCase(final AnimalDeathGateway animalDeathGateway,
                                  final AnimalAccessPolicy accessPolicy) {
        this.animalDeathGateway = animalDeathGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalDeathOutput> execute(final ListAnimalDeathCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        return animalDeathGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListAnimalDeathOutput::from);
    }
}
