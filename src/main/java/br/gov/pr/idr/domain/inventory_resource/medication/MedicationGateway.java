package br.gov.pr.idr.domain.inventory_resource.medication;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface MedicationGateway {

    boolean existsById(final MedicationID id);

    Medication save(final Medication medication);

    Optional<Medication> findById(final MedicationID id);

    Medication update(final Medication medication);

    void deleteById(final MedicationID id);

    Pagination<Medication> search(final AnimalID animalId, final SearchQuery query);
}
