package br.gov.pr.idr.domain.livestock.animalsize;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalSizeGateway {

    boolean existsById(final AnimalSizeID id);

    AnimalSize save(final AnimalSize animalSize);

    Optional<AnimalSize> findById(final AnimalSizeID id);

    AnimalSize update(final AnimalSize animalSize);

    Pagination<AnimalSize> search(final SearchQuery query);

    void deleteById(final AnimalSizeID id);
}
