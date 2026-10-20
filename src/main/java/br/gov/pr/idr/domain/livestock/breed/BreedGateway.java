package br.gov.pr.idr.domain.livestock.breed;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface BreedGateway {

    boolean existsById(final BreedID id);

    Breed save(final Breed breed);

    Optional<Breed> findById(final BreedID id);

    Breed update(final Breed breed);

    Pagination<Breed> search(final SearchQuery query);

    void deleteById(final BreedID id);
}
