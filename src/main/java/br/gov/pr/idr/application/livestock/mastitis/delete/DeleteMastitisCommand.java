package br.gov.pr.idr.application.livestock.mastitis.delete;

import java.util.UUID;

public record DeleteMastitisCommand(
        UUID propertyId,
        UUID animalId,
        UUID mastitisId,
        String username
) {
}
