package br.gov.pr.idr.application.livestock.animal.retrieve.find;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record FindAnimalByIdentifierCommand(
        UUID propertyId,
        String identifier,
        PropertySearchScope scope
) {
}
