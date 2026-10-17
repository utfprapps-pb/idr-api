package br.gov.pr.idr.infra.livestock.insemination.models;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateInseminationRequest(
        @NotNull(message = "A data da inseminação é obrigatória") LocalDate iaDate,
        String bull
) {
}
