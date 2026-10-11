package br.gov.pr.idr.domain.livestock.animal;

import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalGateway {

    Animal save(final Animal animal);

    Optional<Animal> findById(final AnimalID id);

    /** Se o animal existe e pertence à propriedade informada, sem carregá-lo. */
    boolean existsByIdAndPropertyId(final AnimalID id, final PropertyID propertyId);

    Animal update(final Animal animal);

    void deleteById(final AnimalID id);

    Pagination<Animal> search(final PropertyID propertyId, final SearchQuery query);

    Optional<Animal> findByPropertyIdAndIdentifier(final PropertyID propertyId, final String identifier);
}
