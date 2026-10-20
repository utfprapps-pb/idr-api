package br.gov.pr.idr.application.livestock.mastitis.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateMastitisCommand(
        UUID propertyId,
        UUID animalId,
        UUID mastitisId,
        PropertySearchScope scope,
        LocalDate diagnoseDate,
        String mastitisType,
        String cmtResult,
        String ad,
        String ae,
        String pd,
        String pe
) {
}
