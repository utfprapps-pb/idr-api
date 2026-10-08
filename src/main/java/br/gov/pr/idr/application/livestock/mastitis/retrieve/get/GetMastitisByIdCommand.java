package br.gov.pr.idr.application.livestock.mastitis.retrieve.get;

import java.util.UUID;

public record GetMastitisByIdCommand(
        UUID propertyId,
        UUID animalId,
        UUID mastitisId,
        String username
) {
}
