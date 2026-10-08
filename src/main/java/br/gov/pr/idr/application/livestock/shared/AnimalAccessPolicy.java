package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.application.property_management.property.retrieve.search.PropertyAccessPolicy;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;
import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

public final class AnimalAccessPolicy {

    private final PropertyGateway propertyGateway;
    private final UserGateway userGateway;
    private final CityGateway cityGateway;
    private final AnimalGateway animalGateway;

    public AnimalAccessPolicy(final PropertyGateway propertyGateway, final UserGateway userGateway,
                              final CityGateway cityGateway, final AnimalGateway animalGateway) {
        this.propertyGateway = propertyGateway;
        this.userGateway = userGateway;
        this.cityGateway = cityGateway;
        this.animalGateway = animalGateway;
    }

    public Property ensureAccessToProperty(final UUID propertyId, final String username) {
        final var user = userGateway.findByUsername(username)
                .orElseThrow(() -> new UserException("Usuário com o username %s não encontrado".formatted(username)));
        final var id = PropertyID.from(propertyId);
        final var property = propertyGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(Property.class, id));
        final var scope = PropertyAccessPolicy.resolve(user);
        if (!PropertyAccessPolicy.allows(scope, property, cityGateway)) {
            throw ForbiddenException.with("Usuário não tem acesso à propriedade com o ID %s".formatted(propertyId));
        }
        return property;
    }

    public Animal ensureAccessToAnimal(final UUID propertyId, final UUID animalId, final String username) {
        final var property = ensureAccessToProperty(propertyId, username);
        final var id = AnimalID.from(animalId);
        return animalGateway.findById(id)
                .filter(animal -> animal.getPropertyId().equals(property.getId()))
                .orElseThrow(() -> NotFoundException.with(Animal.class, id));
    }
}
