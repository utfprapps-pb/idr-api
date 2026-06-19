package br.gov.pr.idr.application.property_management.property.update;

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
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.collaborator.PropertyCollaborator;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.exceptions.NotFoundException;

@CommandUseCase
public class UpdatePropertyUseCase extends UseCase<UpdatePropertyCommand, UpdatePropertyOutput> {

    private final PropertyGateway propertyGateway;
    private final CityGateway cityGateway;
    private final ProducerGateway producerGateway;

    public UpdatePropertyUseCase(final PropertyGateway propertyGateway, final CityGateway cityGateway,
                                 final ProducerGateway producerGateway) {
        this.propertyGateway = propertyGateway;
        this.cityGateway = cityGateway;
        this.producerGateway = producerGateway;
    }

    @Override
    public UpdatePropertyOutput execute(final UpdatePropertyCommand command) {
        final var propertyId = PropertyID.from(command.id());
        final var cityId = CityID.from(command.cityId());
        final var producerId = ProducerID.from(command.producerId());

        final var property = propertyGateway.findById(propertyId)
                .orElseThrow(() -> NotFoundException.with(Property.class, propertyId));

        if (!cityGateway.existsById(cityId)) {
            throw NotFoundException.with(City.class, cityId);
        }

        if (!producerGateway.existsById(producerId)) {
            throw NotFoundException.with(Producer.class, producerId);
        }

        final var coord = Coord.from(command.latitude(), command.longitude());
        final var technicianIds = command.technicianIds().stream().map(UserID::from).toList();
        final var collaborators = command.collaborators().stream()
                .map(c -> PropertyCollaborator.create(c.name(), c.hoursPerDay()))
                .toList();

        property.update(command.name(), coord, command.nakedAveragePrice(), command.leaseAveragePrice(),
                command.dairyCattleFarming(), command.perennialPasture(), command.summerPlowing(),
                command.winterPlowing(), producerId, cityId, technicianIds, collaborators);

        return UpdatePropertyOutput.from(propertyGateway.update(property));
    }
}
