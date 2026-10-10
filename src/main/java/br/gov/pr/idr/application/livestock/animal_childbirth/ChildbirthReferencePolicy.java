package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

import java.util.UUID;

/** Cross-aggregate checks for what a childbirth points to: the calf's breed and the optional calf animal. */
public final class ChildbirthReferencePolicy {

    private final BreedGateway breedGateway;
    private final AnimalGateway animalGateway;

    public ChildbirthReferencePolicy(final BreedGateway breedGateway, final AnimalGateway animalGateway) {
        this.breedGateway = breedGateway;
        this.animalGateway = animalGateway;
    }

    /** The breed must exist; a linked calf must exist and belong to the mother's property. */
    public void ensureValid(final Animal mother, final UUID breedId, final UUID calfId) {
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
}
