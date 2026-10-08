package br.gov.pr.idr.application.livestock.insemination.retrieve.get;

import java.util.UUID;

public record GetInseminationByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID inseminationId,
        String username
) {
}
