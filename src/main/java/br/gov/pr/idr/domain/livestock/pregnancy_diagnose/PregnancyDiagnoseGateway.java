package br.gov.pr.idr.domain.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface PregnancyDiagnoseGateway {

    PregnancyDiagnose save(final PregnancyDiagnose pregnancyDiagnose);

    Optional<PregnancyDiagnose> findByIdAndAnimalId(final PregnancyDiagnoseID id, final AnimalID animalId);

    PregnancyDiagnose update(final PregnancyDiagnose pregnancyDiagnose);

    void deleteById(final PregnancyDiagnoseID id);

    Pagination<PregnancyDiagnose> search(final AnimalID animalId, final SearchQuery query);
}
