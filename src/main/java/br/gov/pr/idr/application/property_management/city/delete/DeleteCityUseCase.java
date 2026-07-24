package br.gov.pr.idr.application.property_management.city.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeleteCityUseCase extends VoidUseCase<UUID> {

    private final CityGateway cityGateway;

    public DeleteCityUseCase(CityGateway cityGateway) {
        this.cityGateway = cityGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var cityId = CityID.from(id);
        if (!cityGateway.existsById(cityId)) {
            throw NotFoundException.with(City.class, cityId);
        }
        cityGateway.deleteById(cityId);
    }
}
