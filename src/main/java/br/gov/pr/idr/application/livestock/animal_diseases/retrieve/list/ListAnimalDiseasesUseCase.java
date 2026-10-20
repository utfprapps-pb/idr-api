package br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListAnimalDiseasesUseCase extends UseCase<ListAnimalDiseasesCommand,
        Pagination<ListAnimalDiseasesOutput>> {

    private final AnimalDiseasesGateway animalDiseasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalDiseasesUseCase(final AnimalDiseasesGateway animalDiseasesGateway,
                                      final AnimalAccessPolicy accessPolicy) {
        this.animalDiseasesGateway = animalDiseasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalDiseasesOutput> execute(final ListAnimalDiseasesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        return animalDiseasesGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListAnimalDiseasesOutput::from);
    }
}
