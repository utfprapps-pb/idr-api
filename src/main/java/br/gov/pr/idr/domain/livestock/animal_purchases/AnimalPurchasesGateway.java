package br.gov.pr.idr.domain.livestock.animal_purchases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalPurchasesGateway {

    AnimalPurchases save(final AnimalPurchases animalPurchases);

    Optional<AnimalPurchases> findByIdAndAnimalId(final AnimalPurchasesID id, final AnimalID animalId);

    AnimalPurchases update(final AnimalPurchases animalPurchases);

    void deleteById(final AnimalPurchasesID id);

    Pagination<AnimalPurchases> search(final AnimalID animalId, final SearchQuery query);
}
