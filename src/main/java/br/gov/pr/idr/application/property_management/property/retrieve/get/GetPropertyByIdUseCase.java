package br.gov.pr.idr.application.property_management.property.retrieve.get;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@QueryUseCase
public class GetPropertyByIdUseCase extends UseCase<UUID, GetPropertyQueryResult> {

    private final PropertyGateway propertyGateway;

    public GetPropertyByIdUseCase(final PropertyGateway propertyGateway) {
        this.propertyGateway = propertyGateway;
    }

    @Override
    public GetPropertyQueryResult execute(final UUID id) {
        final var propertyId = PropertyID.from(id);
        return propertyGateway.findByIdWithDetails(propertyId)
                .orElseThrow(() -> NotFoundException.with(Property.class, propertyId));
    }
}
