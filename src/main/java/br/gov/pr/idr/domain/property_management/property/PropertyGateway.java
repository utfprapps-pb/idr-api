package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface PropertyGateway {

    Property save(final Property property);

    Property update(final Property property);

    Optional<Property> findById(final PropertyID id);

    Optional<GetPropertyQueryResult> findByIdWithDetails(final PropertyID id);

    Pagination<Property> search(final SearchQuery query, final PropertySearchScope scope);

    boolean existsById(final PropertyID id);

    /** Se a propriedade está dentro do escopo, com os mesmos critérios da busca ({@link #search}). */
    boolean isAccessibleBy(final PropertyID id, final PropertySearchScope scope);

    void deleteById(final PropertyID id);
}
