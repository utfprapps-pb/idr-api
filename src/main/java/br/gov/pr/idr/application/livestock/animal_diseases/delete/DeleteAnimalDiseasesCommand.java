package br.gov.pr.idr.application.livestock.animal_diseases.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record DeleteAnimalDiseasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDiseasesId,
        PropertySearchScope scope
) {
}
