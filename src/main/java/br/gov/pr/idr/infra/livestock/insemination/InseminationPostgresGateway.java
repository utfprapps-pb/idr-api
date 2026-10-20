package br.gov.pr.idr.infra.livestock.insemination;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.insemination.InseminationGateway;
import br.gov.pr.idr.domain.livestock.insemination.InseminationID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.insemination.persistence.InseminationJPAEntity;
import br.gov.pr.idr.infra.livestock.insemination.persistence.InseminationJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InseminationPostgresGateway implements InseminationGateway {

    private final InseminationJPARepository repository;

    @Override
    public Insemination save(final Insemination insemination) {
        return repository.save(InseminationJPAEntity.from(insemination))
                .toAggregate();
    }

    @Override
    public Optional<Insemination> findByIdAndAnimalId(final InseminationID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(InseminationJPAEntity::toAggregate);
    }

    @Override
    public Insemination update(final Insemination insemination) {
        return this.save(insemination);
    }

    @Override
    public void deleteById(final InseminationID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<Insemination> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "iaDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, InseminationJPAEntity::toAggregate);
    }
}
