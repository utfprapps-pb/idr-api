package br.gov.pr.idr.application.livestock.animal_diseases.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalDiseasesCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        String diagnosis,
        LocalDate diagnosisDate
) {
}
