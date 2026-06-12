package br.gov.pr.idr.application.property_management.property.create;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;

@CommandUseCase
public class CreatePropertyUseCase extends UseCase<CreatePropertyCommand, CreatePropertyOutput> {

    private final PropertyGateway propertyGateway;
    private final CityGateway cityGateway;
    private final UserGateway userGateway;

    public CreatePropertyUseCase(final PropertyGateway propertyGateway,
                                 final CityGateway cityGateway,
                                 final UserGateway userGateway
    ) {
        this.propertyGateway = propertyGateway;
        this.cityGateway = cityGateway;
        this.userGateway = userGateway;
    }

    @Override
    public CreatePropertyOutput execute(CreatePropertyCommand command) {
        final var userId = UserID.from(command.producerId());
        final var cityId = CityID.from(command.cityId());
        final var coord = Coord.from(command.latitude(), command.longitude());

        if (!cityGateway.existsById(cityId)) {
            throw NotFoundException.with(City.class, cityId);
        }

        if (!userGateway.existsById(userId)) {
            throw NotFoundException.with(User.class, userId);
        }

        final var property = Property.create(command.name(), coord, command.totalArea(), command.leased(),
                command.nakedAveragePrice(), command.leaseAveragePrice(), command.dairyCattleFarming(),
                command.perennialPasture(), command.summerPlowing(), command.winterPlowing(), userId, cityId);

        return CreatePropertyOutput.from(propertyGateway.save(property));
    }
}
