package br.gov.pr.idr.application.livestock.insemination.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateInseminationCommand(
        UUID propertyId,
        UUID animalId,
        UUID inseminationId,
        PropertySearchScope scope,
        LocalDate iaDate,
        String bull
) {
}
