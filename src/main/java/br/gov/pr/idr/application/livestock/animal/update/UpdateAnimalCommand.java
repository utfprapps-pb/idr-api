package br.gov.pr.idr.application.livestock.animal.update;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        UUID animalMotherId,
        UUID breedId,
        String type,
        String identifier,
        String gender,
        String bornCondition,
        LocalDate bornDate,
        Float bornWeight,
        Float previousWeight,
        Float currentWeight,
        Float ecc
) {
}
