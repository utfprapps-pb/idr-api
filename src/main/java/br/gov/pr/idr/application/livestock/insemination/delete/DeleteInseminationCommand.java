package br.gov.pr.idr.application.livestock.insemination.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record DeleteInseminationCommand(
        UUID propertyId,
        UUID animalId,
        UUID inseminationId,
        PropertySearchScope scope
) {
}
