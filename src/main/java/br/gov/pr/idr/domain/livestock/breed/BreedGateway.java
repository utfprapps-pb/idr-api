package br.gov.pr.idr.domain.livestock.breed;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BreedGateway {

    boolean existsById(final BreedID id);

    Breed save(final Breed breed);

    Optional<Breed> findById(final BreedID id);

    List<Breed> findAllById(final Collection<BreedID> ids);

    /** Breed names for display, in a single lookup; unknown ids are simply absent from the map. */
    default Map<BreedID, String> findNamesByIds(final Collection<BreedID> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        return findAllById(ids).stream().collect(Collectors.toMap(Breed::getId, Breed::getBreedName));
    }

    Breed update(final Breed breed);

    Pagination<Breed> search(final SearchQuery query);

    void deleteById(final BreedID id);
}
