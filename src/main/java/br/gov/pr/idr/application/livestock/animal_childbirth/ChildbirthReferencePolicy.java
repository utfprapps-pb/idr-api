package br.gov.pr.idr.application.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

import java.util.UUID;

/** Validações entre agregados das referências de um parto: a raça do bezerro e o animal bezerro, opcional. */
public final class ChildbirthReferencePolicy {

    private final BreedGateway breedGateway;
    private final AnimalGateway animalGateway;

    public ChildbirthReferencePolicy(final BreedGateway breedGateway, final AnimalGateway animalGateway) {
        this.breedGateway = breedGateway;
        this.animalGateway = animalGateway;
    }

    /**
     * A raça deve existir; um bezerro vinculado deve existir e pertencer à propriedade da mãe. Bezerro igual à
     * própria mãe não é checado aqui: o agregado {@code AnimalChildbirth} já rejeita essa autorreferência.
     */
    public void ensureValid(final Animal mother, final UUID breedId, final UUID calfId) {
        final var notification = NotificationValidation.create();
        if (breedId != null && !breedGateway.existsById(BreedID.from(breedId))) {
            throw new NotificationException("Raça com o ID %s não encontrada".formatted(breedId), notification);
        }
        if (calfId == null || calfId.equals(mother.getId().id())) {
            return;
        }
        if (!animalGateway.existsByIdAndPropertyId(AnimalID.from(calfId), mother.getPropertyId())) {
            throw new NotificationException("Animal (bezerro) com o ID %s não encontrado".formatted(calfId),
                    notification);
        }
    }
}
