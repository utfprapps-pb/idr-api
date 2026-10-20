package br.gov.pr.idr.application.livestock.mastitis.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;

@CommandUseCase
public class CreateMastitisUseCase extends UseCase<CreateMastitisCommand, CreateMastitisOutput> {

    private final MastitisGateway mastitisGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateMastitisUseCase(final MastitisGateway mastitisGateway, final AnimalAccessPolicy accessPolicy) {
        this.mastitisGateway = mastitisGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateMastitisOutput execute(final CreateMastitisCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.scope());

        final var mastitis = Mastitis.create(animal.getId(), command.diagnoseDate(), command.mastitisType(),
                command.cmtResult(), command.ad(), command.ae(), command.pd(), command.pe());

        return CreateMastitisOutput.from(mastitisGateway.save(mastitis));
    }
}
