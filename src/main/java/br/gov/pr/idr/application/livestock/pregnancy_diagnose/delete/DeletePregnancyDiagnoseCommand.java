package br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record DeletePregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        UUID pregnancyDiagnoseId,
        PropertySearchScope scope
) {
}
