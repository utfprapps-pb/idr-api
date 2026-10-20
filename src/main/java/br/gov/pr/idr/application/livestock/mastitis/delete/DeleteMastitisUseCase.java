package br.gov.pr.idr.application.livestock.mastitis.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteMastitisUseCase extends VoidUseCase<DeleteMastitisCommand> {

    private final MastitisGateway mastitisGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteMastitisUseCase(final MastitisGateway mastitisGateway, final AnimalAccessPolicy accessPolicy) {
        this.mastitisGateway = mastitisGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteMastitisCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = MastitisID.from(command.mastitisId());
        final var mastitis = mastitisGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(Mastitis.class, id));

        mastitisGateway.deleteById(mastitis.getId());
    }
}
