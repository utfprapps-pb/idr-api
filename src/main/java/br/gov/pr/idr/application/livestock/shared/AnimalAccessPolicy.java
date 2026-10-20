package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

/**
 * Autorização dos casos de uso de livestock: a propriedade do caminho precisa estar no escopo do usuário e o animal
 * precisa pertencer a ela. Animal de outra propriedade responde como não encontrado, sem revelar que existe.
 */
public final class AnimalAccessPolicy {

    private final PropertyGateway propertyGateway;
    private final AnimalGateway animalGateway;

    public AnimalAccessPolicy(final PropertyGateway propertyGateway, final AnimalGateway animalGateway) {
        this.propertyGateway = propertyGateway;
        this.animalGateway = animalGateway;
    }

    public void ensureAccessToProperty(final UUID propertyId, final PropertySearchScope scope) {
        final var id = PropertyID.from(propertyId);
        if (!propertyGateway.existsById(id)) {
            throw NotFoundException.with(Property.class, id);
        }
        if (!propertyGateway.isAccessibleBy(id, scope)) {
            throw ForbiddenException.with("Usuário não tem acesso à propriedade com o ID %s".formatted(propertyId));
        }
    }

    public Animal ensureAccessToAnimal(final UUID propertyId, final UUID animalId, final PropertySearchScope scope) {
        ensureAccessToProperty(propertyId, scope);
        final var id = AnimalID.from(animalId);
        return animalGateway.findById(id)
                .filter(animal -> animal.getPropertyId().equals(PropertyID.from(propertyId)))
                .orElseThrow(() -> NotFoundException.with(Animal.class, id));
    }
}
