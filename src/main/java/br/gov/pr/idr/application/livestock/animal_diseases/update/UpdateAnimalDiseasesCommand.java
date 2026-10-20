package br.gov.pr.idr.application.livestock.animal_diseases.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalDiseasesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalDiseasesId,
        PropertySearchScope scope,
        String diagnosis,
        LocalDate diagnosisDate
) {
}
