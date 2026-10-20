package br.gov.pr.idr.infra.livestock.animal_death;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathGateway;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeathID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_death.persistence.AnimalDeathJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_death.persistence.AnimalDeathJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalDeathPostgresGateway implements AnimalDeathGateway {

    private final AnimalDeathJPARepository repository;

    @Override
    public AnimalDeath save(final AnimalDeath animalDeath) {
        return repository.save(AnimalDeathJPAEntity.from(animalDeath))
                .toAggregate();
    }

    @Override
    public Optional<AnimalDeath> findByIdAndAnimalId(final AnimalDeathID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(AnimalDeathJPAEntity::toAggregate);
    }

    @Override
    public AnimalDeath update(final AnimalDeath animalDeath) {
        return this.save(animalDeath);
    }

    @Override
    public void deleteById(final AnimalDeathID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public boolean existsByAnimalId(final AnimalID animalId) {
        return repository.existsByAnimalId(animalId.id());
    }

    @Override
    public Pagination<AnimalDeath> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "deathDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, AnimalDeathJPAEntity::toAggregate);
    }
}
