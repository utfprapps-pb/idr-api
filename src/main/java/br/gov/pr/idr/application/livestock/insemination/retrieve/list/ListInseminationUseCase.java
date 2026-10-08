package br.gov.pr.idr.application.livestock.insemination.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListInseminationUseCase extends UseCase<ListInseminationCommand, Pagination<ListInseminationOutput>> {

    private final InseminationGateway inseminationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListInseminationUseCase(final InseminationGateway inseminationGateway,
                                    final AnimalAccessPolicy accessPolicy) {
        this.inseminationGateway = inseminationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListInseminationOutput> execute(final ListInseminationCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        return inseminationGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListInseminationOutput::from);
    }
}
