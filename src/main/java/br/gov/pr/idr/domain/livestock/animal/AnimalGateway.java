package br.gov.pr.idr.domain.livestock.animal;

import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AnimalGateway {

    Animal save(final Animal animal);

    Optional<Animal> findById(final AnimalID id);

    List<Animal> findAllById(final Collection<AnimalID> ids);

    Animal update(final Animal animal);

    void deleteById(final AnimalID id);

    Pagination<Animal> search(final PropertyID propertyId, final SearchQuery query);

    Optional<Animal> findByPropertyIdAndIdentifier(final PropertyID propertyId, final String identifier);
}
