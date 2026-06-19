package br.gov.pr.idr.application.property_management.property.create;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerGateway;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;

@CommandUseCase
public class CreatePropertyUseCase extends UseCase<CreatePropertyCommand, CreatePropertyOutput> {

    private final PropertyGateway propertyGateway;
    private final CityGateway cityGateway;
    private final ProducerGateway producerGateway;

    public CreatePropertyUseCase(final PropertyGateway propertyGateway,
                                 final CityGateway cityGateway,
                                 final ProducerGateway producerGateway
    ) {
        this.propertyGateway = propertyGateway;
        this.cityGateway = cityGateway;
        this.producerGateway = producerGateway;
    }

    @Override
    public CreatePropertyOutput execute(CreatePropertyCommand command) {
        final var producerId = ProducerID.from(command.producerId());
        final var cityId = CityID.from(command.cityId());
        final var coord = Coord.from(command.latitude(), command.longitude());

        if (!cityGateway.existsById(cityId)) {
            throw NotFoundException.with(City.class, cityId);
        }

        if (!producerGateway.existsById(producerId)) {
            throw NotFoundException.with(Producer.class, producerId);
        }

        final var technicianIds = command.technicianIds().stream().map(UserID::from).toList();
        final var collaborators = command.collaborators().stream()
                .map(c -> PropertyCollaborator.create(c.name(), c.hoursPerDay()))
                .toList();

        final var property = Property.create(command.name(), coord,
                command.nakedAveragePrice(), command.leaseAveragePrice(), command.dairyCattleFarming(),
                command.perennialPasture(), command.summerPlowing(), command.winterPlowing(), producerId, cityId,
                technicianIds, collaborators);

        return CreatePropertyOutput.from(propertyGateway.save(property));
    }
}
