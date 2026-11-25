package br.gov.pr.idr.infra.livestock.animalsize;

import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeGateway;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animalsize.persistence.AnimalSizeJPAEntity;
import br.gov.pr.idr.infra.livestock.animalsize.persistence.AnimalSizeJPARepository;
import br.gov.pr.idr.infra.livestock.animalsize.persistence.AnimalSizeSpecification;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalSizePostgresGateway implements AnimalSizeGateway {

    private final AnimalSizeJPARepository repository;

    @Override
    public boolean existsById(final AnimalSizeID id) {
        return repository.existsById(id.id());
    }

    @Override
    public AnimalSize save(final AnimalSize animalSize) {
        return repository.save(AnimalSizeJPAEntity.from(animalSize))
                .toAggregate();
    }

    @Override
    public Optional<AnimalSize> findById(final AnimalSizeID id) {
        return repository.findById(id.id()).map(AnimalSizeJPAEntity::toAggregate);
    }

    @Override
    public AnimalSize update(final AnimalSize animalSize) {
        return this.save(animalSize);
    }

    @Override
    public Pagination<AnimalSize> search(final SearchQuery query) {
        final var terms = PageRequestFactory.terms(query.terms());
        final var pageRequest = PageRequestFactory.from(query, "sizeName");
        final var page = repository.findAll(AnimalSizeSpecification.withTerms(terms), pageRequest);
        return PaginationFactory.from(page, AnimalSizeJPAEntity::toAggregate);
    }

    @Override
    public void deleteById(final AnimalSizeID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }
}
