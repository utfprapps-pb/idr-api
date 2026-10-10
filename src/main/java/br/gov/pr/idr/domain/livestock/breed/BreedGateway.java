package br.gov.pr.idr.domain.livestock.breed;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface BreedGateway {

    boolean existsById(final BreedID id);

    Breed save(final Breed breed);

    Optional<Breed> findById(final BreedID id);

    /** Breed names for display, in one lookup; unknown ids are absent from the map. */
    Map<BreedID, String> findNamesByIds(final Collection<BreedID> ids);

    Breed update(final Breed breed);

    Pagination<Breed> search(final SearchQuery query);

    void deleteById(final BreedID id);
}
