package br.gov.pr.idr.application.livestock.pregnancy_diagnose.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePregnancyDiagnoseCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        LocalDate diagnoseDate,
        LocalDate lastIa
) {
}
