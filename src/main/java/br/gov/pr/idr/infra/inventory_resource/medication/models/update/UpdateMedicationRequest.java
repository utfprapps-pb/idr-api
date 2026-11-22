package br.gov.pr.idr.infra.inventory_resource.medication.models.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMedicationRequest(
        UUID productId,
        @Size(max = 255, message = "O produto da medicação deve ter no máximo 255 caracteres") String productName,
        @Size(max = 255, message = "O princípio ativo da medicação deve ter no máximo 255 caracteres")
        String activePrinciple,
        @NotNull(message = "A data da aplicação é obrigatória") LocalDate applicationDate,
        String appliedDose,
        String applicationWay
) {
}
