package br.gov.pr.idr.application.property_management.property.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeletePropertyUseCase extends VoidUseCase<UUID> {

    private final PropertyGateway propertyGateway;

    public DeletePropertyUseCase(PropertyGateway propertyGateway) {
        this.propertyGateway = propertyGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var propertyId = PropertyID.from(id);
        if (!propertyGateway.existsById(propertyId)) {
            throw NotFoundException.with(Property.class, propertyId);
        }
        propertyGateway.deleteById(propertyId);
    }
}
