package br.gov.pr.idr.infra.livestock.breed;

import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPAEntity;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedSpecification;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BreedPostgresGateway implements BreedGateway {

    private final BreedJPARepository repository;

    @Override
    public boolean existsById(final BreedID id) {
        return repository.existsById(id.id());
    }

    @Override
    public Breed save(final Breed breed) {
        return repository.save(BreedJPAEntity.from(breed))
                .toAggregate();
    }

    @Override
    public Optional<Breed> findById(final BreedID id) {
        return repository.findById(id.id()).map(BreedJPAEntity::toAggregate);
    }

    @Override
    public Breed update(final Breed breed) {
        return this.save(breed);
    }

    @Override
    public Pagination<Breed> search(final SearchQuery query) {
        final var terms = PageRequestFactory.terms(query.terms());
        final var pageRequest = PageRequestFactory.from(query, "breedName");
        final var page = repository.findAll(BreedSpecification.withTerms(terms), pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.map(BreedJPAEntity::toAggregate).toList());
    }

    @Override
    public void deleteById(final BreedID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }
}
