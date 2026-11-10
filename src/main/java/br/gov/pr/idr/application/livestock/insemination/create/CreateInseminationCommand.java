package br.gov.pr.idr.application.livestock.insemination.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record CreateInseminationCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        LocalDate iaDate,
        String bull
) {
}
