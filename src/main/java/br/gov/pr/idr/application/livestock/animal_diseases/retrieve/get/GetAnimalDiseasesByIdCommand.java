package br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record GetAnimalDiseasesByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDiseasesId,
        PropertySearchScope scope
) {
}
