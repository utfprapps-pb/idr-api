package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/** Checks and resolves what a childbirth points to: the calf's breed and the optional calf animal. */
public final class ChildbirthReferencePolicy {

    private ChildbirthReferencePolicy() {
    }

    /** The breed must exist; a linked calf must exist and belong to the mother's property. */
    public static void ensureValid(final BreedGateway breedGateway, final AnimalGateway animalGateway,
                                   final Animal mother, final UUID breedId, final UUID calfId) {
        final var notification = NotificationValidation.create();
        if (breedId != null && !breedGateway.existsById(BreedID.from(breedId))) {
            throw new NotificationException("Raça com o ID %s não encontrada".formatted(breedId), notification);
        }
        if (calfId == null || calfId.equals(mother.getId().id())) {
            return; // no calf linked, or a self-reference that the aggregate itself rejects
        }
        final var calfBelongsToProperty = animalGateway.findById(AnimalID.from(calfId))
                .filter(calf -> calf.getPropertyId().equals(mother.getPropertyId()))
                .isPresent();
        if (!calfBelongsToProperty) {
            throw new NotificationException("Animal (bezerro) com o ID %s não encontrado".formatted(calfId),
                    notification);
        }
    }

    public static Map<BreedID, String> breedNames(final BreedGateway breedGateway,
                                                  final Collection<AnimalChildbirth> childbirths) {
        final var ids = childbirths.stream().map(AnimalChildbirth::getBreedId).collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return breedGateway.findAllById(ids).stream()
                .collect(Collectors.toMap(Breed::getId, Breed::getBreedName));
    }

    public static Map<AnimalID, String> calfIdentifiers(final AnimalGateway animalGateway,
                                                        final Collection<AnimalChildbirth> childbirths) {
        final var ids = childbirths.stream()
                .flatMap(childbirth -> childbirth.getCalfId().stream())
                .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return animalGateway.findAllById(ids).stream()
                .collect(Collectors.toMap(Animal::getId, Animal::getIdentifier));
    }
}
