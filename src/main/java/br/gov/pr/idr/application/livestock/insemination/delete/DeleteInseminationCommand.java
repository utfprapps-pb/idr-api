package br.gov.pr.idr.application.livestock.insemination.delete;

import java.util.UUID;

public record DeleteInseminationCommand(
        UUID propertyId,
        UUID animalId,
        UUID inseminationId,
        String username
) {
}
