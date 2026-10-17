package br.gov.pr.idr.application.livestock.mastitis.update;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMastitisCommand(
        UUID propertyId,
        UUID animalId,
        UUID mastitisId,
        String username,
        LocalDate diagnoseDate,
        String mastitisType,
        String cmtResult,
        String ad,
        String ae,
        String pd,
        String pe
) {
}
