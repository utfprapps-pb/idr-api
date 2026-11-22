package br.gov.pr.idr.infra.inventory_resource.medication.models;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMedicationRequest(
        UUID productId,
        String productName,
        String activePrinciple,
        @NotNull(message = "A data da aplicação é obrigatória") LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
