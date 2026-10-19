package br.gov.pr.idr.application.livestock.insemination.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteInseminationUseCase extends VoidUseCase<DeleteInseminationCommand> {

    private final InseminationGateway inseminationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteInseminationUseCase(final InseminationGateway inseminationGateway,
                                      final AnimalAccessPolicy accessPolicy) {
        this.inseminationGateway = inseminationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteInseminationCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = InseminationID.from(command.inseminationId());
        final var insemination = inseminationGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(Insemination.class, id));

        inseminationGateway.deleteById(insemination.getId());
    }
}
