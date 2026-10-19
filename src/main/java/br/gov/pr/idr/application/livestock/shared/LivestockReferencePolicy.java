package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

import java.util.UUID;

/**
 * Validações entre agregados das referências de livestock: a raça precisa existir e o animal referenciado (mãe,
 * bezerro) precisa ser da mesma propriedade. Referência {@code null} não é checada aqui; a obrigatoriedade e a
 * autorreferência ficam com o próprio agregado.
 */
public final class LivestockReferencePolicy {

    private final BreedGateway breedGateway;
    private final AnimalGateway animalGateway;

    public LivestockReferencePolicy(final BreedGateway breedGateway, final AnimalGateway animalGateway) {
        this.breedGateway = breedGateway;
        this.animalGateway = animalGateway;
    }

    public void ensureBreedExists(final UUID breedId) {
        if (breedId != null && !breedGateway.existsById(BreedID.from(breedId))) {
            throw new NotificationException("Raça com o ID %s não encontrada".formatted(breedId),
                    NotificationValidation.create());
        }
    }

    /** {@code role} nomeia a referência na mensagem de erro (ex.: "mãe", "bezerro"). */
    public void ensureAnimalInProperty(final UUID animalId, final PropertyID propertyId, final String role) {
        if (animalId != null && !animalGateway.existsByIdAndPropertyId(AnimalID.from(animalId), propertyId)) {
            throw new NotificationException("Animal (%s) com o ID %s não encontrado".formatted(role, animalId),
                    NotificationValidation.create());
        }
    }
}
