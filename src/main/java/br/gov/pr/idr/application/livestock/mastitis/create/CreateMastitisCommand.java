package br.gov.pr.idr.application.livestock.mastitis.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import java.time.LocalDate;
import java.util.UUID;

public record CreateMastitisCommand(
        UUID propertyId,
        UUID animalId,
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
