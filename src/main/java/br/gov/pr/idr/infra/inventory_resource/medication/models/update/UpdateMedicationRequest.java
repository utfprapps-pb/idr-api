package br.gov.pr.idr.infra.inventory_resource.medication.models.update;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMedicationRequest(
        @NotNull(message = "O produto da medicação é obrigatório") UUID productId,
        @NotNull(message = "A data da aplicação é obrigatória") LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
