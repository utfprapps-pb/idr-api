package br.gov.pr.idr.application.livestock.pregnancy_diagnose.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        UUID pregnancyDiagnoseId,
        PropertySearchScope scope,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {
}
