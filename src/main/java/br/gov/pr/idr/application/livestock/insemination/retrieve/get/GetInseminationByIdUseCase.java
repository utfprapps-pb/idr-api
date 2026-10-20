package br.gov.pr.idr.application.livestock.insemination.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetInseminationByIdUseCase extends UseCase<GetInseminationByIdCommand, GetInseminationByIdOutput> {

    private final InseminationGateway inseminationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetInseminationByIdUseCase(final InseminationGateway inseminationGateway,
                                       final AnimalAccessPolicy accessPolicy) {
        this.inseminationGateway = inseminationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetInseminationByIdOutput execute(final GetInseminationByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = InseminationID.from(command.inseminationId());
        final var insemination = inseminationGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(Insemination.class, id));

        return GetInseminationByIdOutput.from(insemination);
    }
}
