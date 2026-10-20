package br.gov.pr.idr.application.livestock.animal.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record GetAnimalByIdCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope
) {
}
