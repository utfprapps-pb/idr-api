package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record GetAnimalChildbirthByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalChildbirthId,
        PropertySearchScope scope
) {
}
