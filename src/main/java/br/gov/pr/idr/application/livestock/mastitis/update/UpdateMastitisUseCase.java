package br.gov.pr.idr.application.livestock.mastitis.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateMastitisUseCase extends UseCase<UpdateMastitisCommand, UpdateMastitisOutput> {

    private final MastitisGateway mastitisGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateMastitisUseCase(final MastitisGateway mastitisGateway, final AnimalAccessPolicy accessPolicy) {
        this.mastitisGateway = mastitisGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateMastitisOutput execute(final UpdateMastitisCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = MastitisID.from(command.mastitisId());
        final var mastitis = mastitisGateway.findById(id)
                .filter(m -> m.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(Mastitis.class, id));

        final var updatedMastitis = mastitis.update(command.diagnoseDate(), command.mastitisType(),
                command.cmtResult(), command.ad(), command.ae(), command.pd(), command.pe());

        return UpdateMastitisOutput.from(mastitisGateway.update(updatedMastitis));
    }
}
