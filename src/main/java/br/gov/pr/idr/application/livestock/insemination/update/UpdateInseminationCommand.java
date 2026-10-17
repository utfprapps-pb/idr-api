package br.gov.pr.idr.application.livestock.insemination.update;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateInseminationCommand(
        UUID propertyId,
        UUID animalId,
        UUID inseminationId,
        String username,
        LocalDate iaDate,
        String bull
) {
}
