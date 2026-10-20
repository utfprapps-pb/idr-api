package br.gov.pr.idr.application.livestock.mastitis.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListMastitisUseCase extends UseCase<ListMastitisCommand, Pagination<ListMastitisOutput>> {

    private final MastitisGateway mastitisGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListMastitisUseCase(final MastitisGateway mastitisGateway, final AnimalAccessPolicy accessPolicy) {
        this.mastitisGateway = mastitisGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListMastitisOutput> execute(final ListMastitisCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        return mastitisGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListMastitisOutput::from);
    }
}
