package br.gov.pr.idr.application.livestock.insemination.create;

import java.time.LocalDate;
import java.util.UUID;

public record CreateInseminationCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        LocalDate iaDate,
        String bull
) {
}
