package br.gov.pr.idr.application.livestock.mastitis.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetMastitisByIdUseCase extends UseCase<GetMastitisByIdCommand, GetMastitisByIdOutput> {

    private final MastitisGateway mastitisGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetMastitisByIdUseCase(final MastitisGateway mastitisGateway, final AnimalAccessPolicy accessPolicy) {
        this.mastitisGateway = mastitisGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetMastitisByIdOutput execute(final GetMastitisByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = MastitisID.from(command.mastitisId());
        final var mastitis = mastitisGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(Mastitis.class, id));

        return GetMastitisByIdOutput.from(mastitis);
    }
}
