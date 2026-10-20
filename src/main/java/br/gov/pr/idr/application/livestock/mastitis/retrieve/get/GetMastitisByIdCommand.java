package br.gov.pr.idr.application.livestock.mastitis.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record GetMastitisByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID mastitisId,
        PropertySearchScope scope
) {
}
