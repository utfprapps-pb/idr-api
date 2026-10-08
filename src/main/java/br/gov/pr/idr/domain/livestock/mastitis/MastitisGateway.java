package br.gov.pr.idr.domain.livestock.mastitis;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface MastitisGateway {

    boolean existsById(final MastitisID id);

    Mastitis save(final Mastitis mastitis);

    Optional<Mastitis> findById(final MastitisID id);

    Mastitis update(final Mastitis mastitis);

    void deleteById(final MastitisID id);

    Pagination<Mastitis> search(final AnimalID animalId, final SearchQuery query);
}
