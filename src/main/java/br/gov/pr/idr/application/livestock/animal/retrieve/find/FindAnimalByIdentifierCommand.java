package br.gov.pr.idr.application.livestock.animal.retrieve.find;

import java.util.UUID;

public record FindAnimalByIdentifierCommand(
        UUID propertyId,
        String identifier,
        String username
) {
}
