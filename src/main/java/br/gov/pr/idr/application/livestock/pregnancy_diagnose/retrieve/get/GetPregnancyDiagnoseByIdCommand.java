package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record GetPregnancyDiagnoseByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID pregnancyDiagnoseId,
        PropertySearchScope scope
) {
}
