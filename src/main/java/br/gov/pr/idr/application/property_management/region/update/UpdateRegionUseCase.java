package br.gov.pr.idr.application.property_management.region.update;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

@QueryUseCase
public class UpdateRegionUseCase extends UseCase<UpdateRegionCommand, UpdateRegionOutput> {

    private final RegionGateway regionGateway;

    public UpdateRegionUseCase(RegionGateway regionGateway) {
        this.regionGateway = regionGateway;
    }

    @Override
    public UpdateRegionOutput execute(UpdateRegionCommand command) {
        final var id = RegionID.from(command.id());
        final var description = command.description();
        NotificationValidation notification = NotificationValidation.create();

        final var region = regionGateway.findByID(id).orElseThrow(
                () -> new NotificationException("Não foi encontrado cidade com o id %s"
                        .formatted(id.id()), notification)).update(description);

        if (regionGateway.existsByDescription(description)) {
            throw new NotificationException("Já existe uma região com a descrição %s".formatted(description),
                    notification);
        }
        return UpdateRegionOutput.from(regionGateway.update(region));
    }
}
