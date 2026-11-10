package br.gov.pr.idr.application.livestock.mastitis.delete;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.util.UUID;

public record DeleteMastitisCommand(
        UUID propertyId,
        UUID animalId,
        UUID mastitisId,
        PropertySearchScope scope
) {
}
