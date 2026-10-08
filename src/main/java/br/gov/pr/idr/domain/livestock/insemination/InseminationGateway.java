package br.gov.pr.idr.domain.livestock.insemination;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface InseminationGateway {

    boolean existsById(final InseminationID id);

    Insemination save(final Insemination insemination);

    Optional<Insemination> findById(final InseminationID id);

    Insemination update(final Insemination insemination);

    void deleteById(final InseminationID id);

    Pagination<Insemination> search(final AnimalID animalId, final SearchQuery query);
}
