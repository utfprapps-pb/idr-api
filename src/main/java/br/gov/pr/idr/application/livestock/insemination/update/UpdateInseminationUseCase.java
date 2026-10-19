package br.gov.pr.idr.application.livestock.insemination.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateInseminationUseCase extends UseCase<UpdateInseminationCommand, UpdateInseminationOutput> {

    private final InseminationGateway inseminationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateInseminationUseCase(final InseminationGateway inseminationGateway,
                                      final AnimalAccessPolicy accessPolicy) {
        this.inseminationGateway = inseminationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateInseminationOutput execute(final UpdateInseminationCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = InseminationID.from(command.inseminationId());
        final var insemination = inseminationGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(Insemination.class, id));

        final var updatedInsemination = insemination.update(command.iaDate(), command.bull());

        return UpdateInseminationOutput.from(inseminationGateway.update(updatedInsemination));
    }
}
