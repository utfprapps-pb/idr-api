package br.gov.pr.idr.infra.livestock.mastitis.models;

import java.time.LocalDate;

public record CreateMastitisRequest(
        LocalDate diagnoseDate,
        String mastitisType,
        String cmtResult,
        String ad,
        String ae,
        String pd,
        String pe
) {
}
