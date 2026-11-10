package br.gov.pr.idr.infra.livestock.animal_sales;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesGateway;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSalesID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_sales.persistence.AnimalSalesJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_sales.persistence.AnimalSalesJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalSalesPostgresGateway implements AnimalSalesGateway {

    private final AnimalSalesJPARepository repository;

    @Override
    public AnimalSales save(final AnimalSales animalSales) {
        return repository.save(AnimalSalesJPAEntity.from(animalSales))
                .toAggregate();
    }

    @Override
    public Optional<AnimalSales> findByIdAndAnimalId(final AnimalSalesID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(AnimalSalesJPAEntity::toAggregate);
    }

    @Override
    public AnimalSales update(final AnimalSales animalSales) {
        return this.save(animalSales);
    }

    @Override
    public void deleteById(final AnimalSalesID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<AnimalSales> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "dateSale");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, AnimalSalesJPAEntity::toAggregate);
    }
}
