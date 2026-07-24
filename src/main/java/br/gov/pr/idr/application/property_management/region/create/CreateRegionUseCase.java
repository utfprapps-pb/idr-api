package br.gov.pr.idr.application.property_management.region.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

@CommandUseCase
public class CreateRegionUseCase extends UseCase<CreateRegionCommand, CreateRegionOutput> {

    private final RegionGateway regionGateway;

    public CreateRegionUseCase(RegionGateway regionGateway) {
        this.regionGateway = regionGateway;
    }

    @Override
    public CreateRegionOutput execute(CreateRegionCommand command) {
        final var description = command.description();
        final var region = Region.create(description);
        if (regionGateway.existsByDescription(description)) {
            throw new NotificationException("Já existe uma região com a descrição %s".formatted(description),
                    NotificationValidation.create());
        }
        return CreateRegionOutput.from(regionGateway.save(region));
    }
}
