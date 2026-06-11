package br.gov.pr.idr.application.property_management.city.update;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

@QueryUseCase
public class UpdateCityUseCase extends UseCase<UpdateCityCommand, UpdateCityOutput> {

    private final CityGateway cityGateway;
    private final RegionGateway regionGateway;

    public UpdateCityUseCase(CityGateway cityGateway, RegionGateway regionGateway) {
        this.cityGateway = cityGateway;
        this.regionGateway = regionGateway;
    }

    @Override
    public UpdateCityOutput execute(final UpdateCityCommand command) {
        final var cityId = CityID.from(command.regionId());
        final var regionId = RegionID.from(command.regionId());
        final var notification = NotificationValidation.create();

        final var city = cityGateway.findById(cityId).orElseThrow(
                () -> new NotificationException("Cidade com o id %s não encontrada".formatted(cityId.id()),
                        notification)).update(regionId);

        if (!regionGateway.existsById(regionId)) {
            throw new NotificationException("Região com o id %s não encontrada".formatted(regionId.id()),
                    notification);
        }
        return UpdateCityOutput.from(cityGateway.update(city));
    }
}
