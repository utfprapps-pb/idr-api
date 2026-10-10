package br.gov.pr.idr.domain.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalChildbirthGateway {

    AnimalChildbirth save(final AnimalChildbirth animalChildbirth);

    Optional<AnimalChildbirth> findById(final AnimalChildbirthID id);

    AnimalChildbirth update(final AnimalChildbirth animalChildbirth);

    void deleteById(final AnimalChildbirthID id);

    Pagination<AnimalChildbirth> search(final AnimalID animalId, final SearchQuery query);
}
