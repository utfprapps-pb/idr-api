package br.gov.pr.idr.domain.livestock.animal_death;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalDeathGateway {

    AnimalDeath save(final AnimalDeath animalDeath);

    Optional<AnimalDeath> findByIdAndAnimalId(final AnimalDeathID id, final AnimalID animalId);

    AnimalDeath update(final AnimalDeath animalDeath);

    void deleteById(final AnimalDeathID id);

    boolean existsByAnimalId(final AnimalID animalId);

    Pagination<AnimalDeath> search(final AnimalID animalId, final SearchQuery query);
}
