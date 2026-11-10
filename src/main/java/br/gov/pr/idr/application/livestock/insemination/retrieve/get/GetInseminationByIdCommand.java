package br.gov.pr.idr.application.livestock.insemination.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record GetInseminationByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID inseminationId,
        PropertySearchScope scope
) {
}
