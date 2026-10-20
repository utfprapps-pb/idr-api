package br.gov.pr.idr.application.inventory_resource.medication.retrieve.get;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.util.UUID;

public record GetMedicationByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID medicationId,
        PropertySearchScope scope
) {
}
