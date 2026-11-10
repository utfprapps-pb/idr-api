package br.gov.pr.idr.infra.livestock.mastitis.models.update;

import java.time.LocalDate;

public record UpdateMastitisRequest(
        LocalDate diagnoseDate,
        String mastitisType,
        String cmtResult,
        String ad,
        String ae,
        String pd,
        String pe
) {
}
