package br.gov.pr.idr.infra.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_childbirth.persistence.AnimalChildbirthJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_childbirth.persistence.AnimalChildbirthJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalChildbirthPostgresGateway implements AnimalChildbirthGateway {

    private final AnimalChildbirthJPARepository repository;

    @Override
    public AnimalChildbirth save(final AnimalChildbirth animalChildbirth) {
        return repository.save(AnimalChildbirthJPAEntity.from(animalChildbirth))
                .toAggregate();
    }

    @Override
    public Optional<AnimalChildbirth> findByIdAndAnimalId(final AnimalChildbirthID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(AnimalChildbirthJPAEntity::toAggregate);
    }

    @Override
    public AnimalChildbirth update(final AnimalChildbirth animalChildbirth) {
        return this.save(animalChildbirth);
    }

    @Override
    public void deleteById(final AnimalChildbirthID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<AnimalChildbirth> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "childbirthDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, AnimalChildbirthJPAEntity::toAggregate);
    }
}
