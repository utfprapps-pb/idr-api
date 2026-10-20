package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.application.property_management.property.PropertyAccessPolicy;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

/**
 * Autorização dos casos de uso de livestock: o usuário precisa ter acesso à propriedade do caminho e o animal precisa
 * pertencer a ela. Animal de outra propriedade responde como não encontrado, sem revelar que existe.
 */
public final class AnimalAccessPolicy {

    private final PropertyGateway propertyGateway;
    private final UserGateway userGateway;
    private final AnimalGateway animalGateway;

    public AnimalAccessPolicy(final PropertyGateway propertyGateway, final UserGateway userGateway,
                              final AnimalGateway animalGateway) {
        this.propertyGateway = propertyGateway;
        this.userGateway = userGateway;
        this.animalGateway = animalGateway;
    }

    public void ensureAccessToProperty(final UUID propertyId, final String username) {
        final var user = userGateway.findByUsername(username)
                .orElseThrow(() -> new UserException("Usuário com o username %s não encontrado".formatted(username)));
        final var id = PropertyID.from(propertyId);
        if (!propertyGateway.existsById(id)) {
            throw NotFoundException.with(Property.class, id);
        }
        if (!propertyGateway.isAccessibleBy(id, PropertyAccessPolicy.resolve(user))) {
            throw ForbiddenException.with("Usuário não tem acesso à propriedade com o ID %s".formatted(propertyId));
        }
    }

    public Animal ensureAccessToAnimal(final UUID propertyId, final UUID animalId, final String username) {
        ensureAccessToProperty(propertyId, username);
        final var id = AnimalID.from(animalId);
        return animalGateway.findById(id)
                .filter(animal -> animal.getPropertyId().equals(PropertyID.from(propertyId)))
                .orElseThrow(() -> NotFoundException.with(Animal.class, id));
    }
}
