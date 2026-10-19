package br.gov.pr.idr.domain.livestock.animal_diseases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalDiseasesGateway {

    AnimalDiseases save(final AnimalDiseases animalDiseases);

    Optional<AnimalDiseases> findByIdAndAnimalId(final AnimalDiseasesID id, final AnimalID animalId);

    AnimalDiseases update(final AnimalDiseases animalDiseases);

    void deleteById(final AnimalDiseasesID id);

    Pagination<AnimalDiseases> search(final AnimalID animalId, final SearchQuery query);
}
