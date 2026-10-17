package br.gov.pr.idr.domain.livestock.animal_sales;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface AnimalSalesGateway {

    AnimalSales save(final AnimalSales animalSales);

    Optional<AnimalSales> findById(final AnimalSalesID id);

    AnimalSales update(final AnimalSales animalSales);

    void deleteById(final AnimalSalesID id);

    Pagination<AnimalSales> search(final AnimalID animalId, final SearchQuery query);
}
