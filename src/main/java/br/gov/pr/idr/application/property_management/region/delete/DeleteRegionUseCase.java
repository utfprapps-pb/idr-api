package br.gov.pr.idr.application.property_management.region.delete;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.VoidUseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

import java.util.UUID;

@CommandUseCase
public class DeleteRegionUseCase extends VoidUseCase<UUID> {

    private final RegionGateway regionGateway;
    private final CityGateway cityGateway;

    public DeleteRegionUseCase(RegionGateway regionGateway, CityGateway cityGateway) {
        this.regionGateway = regionGateway;
        this.cityGateway = cityGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var regionId = RegionID.from(id);
        if (!regionGateway.existsById(regionId)) {
            throw NotFoundException.with(Region.class, regionId);
        }
        if (cityGateway.existsByRegionId(regionId)) {
            throw new NotificationException(
                    "Região com o id %s possui cidades vinculadas e não pode ser removida".formatted(id),
                    NotificationValidation.create()
            );
        }
        regionGateway.deleteById(regionId);
    }
}
