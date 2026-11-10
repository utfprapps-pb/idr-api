package br.gov.pr.idr.infra.livestock.animal_purchases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesGateway;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchasesID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_purchases.persistence.AnimalPurchasesJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_purchases.persistence.AnimalPurchasesJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalPurchasesPostgresGateway implements AnimalPurchasesGateway {

    private final AnimalPurchasesJPARepository repository;

    @Override
    public AnimalPurchases save(final AnimalPurchases animalPurchases) {
        return repository.save(AnimalPurchasesJPAEntity.from(animalPurchases))
                .toAggregate();
    }

    @Override
    public Optional<AnimalPurchases> findByIdAndAnimalId(final AnimalPurchasesID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(AnimalPurchasesJPAEntity::toAggregate);
    }

    @Override
    public AnimalPurchases update(final AnimalPurchases animalPurchases) {
        return this.save(animalPurchases);
    }

    @Override
    public void deleteById(final AnimalPurchasesID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<AnimalPurchases> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "datePurchase");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, AnimalPurchasesJPAEntity::toAggregate);
    }
}
