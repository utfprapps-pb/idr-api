package br.gov.pr.idr.application.property_management.city.create;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

@CommandUseCase
public class CreateCityUseCase extends UseCase<CreateCityCommand, CreateCityOutput> {

    private final CityGateway cityGateway;
    private final RegionGateway regionGateway;

    public CreateCityUseCase(CityGateway cityGateway, RegionGateway regionGateway) {
        this.cityGateway = cityGateway;
        this.regionGateway = regionGateway;
    }

    @Override
    public CreateCityOutput execute(final CreateCityCommand command) {
        final var name = command.name();
        final var state = command.state();
        final var regionId = RegionID.from(command.regionId());
        final var city = City.create(name, state, regionId);
        if (!regionGateway.existsById(regionId)) {
            throw new NotificationException("Região com o id %s não encontrada".formatted(regionId.id()),
                    NotificationValidation.create());
        }
        return CreateCityOutput.from(cityGateway.save(city));
    }
}
