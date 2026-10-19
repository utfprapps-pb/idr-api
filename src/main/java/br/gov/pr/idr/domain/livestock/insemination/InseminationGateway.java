package br.gov.pr.idr.domain.livestock.insemination;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface InseminationGateway {

    Insemination save(final Insemination insemination);

    Optional<Insemination> findByIdAndAnimalId(final InseminationID id, final AnimalID animalId);

    Insemination update(final Insemination insemination);

    void deleteById(final InseminationID id);

    Pagination<Insemination> search(final AnimalID animalId, final SearchQuery query);
}
