package br.gov.pr.idr.application.livestock.mastitis.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMastitisCommand(
        UUID propertyId,
        UUID animalId,
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
