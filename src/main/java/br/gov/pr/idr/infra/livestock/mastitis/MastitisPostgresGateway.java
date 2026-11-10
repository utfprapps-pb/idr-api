package br.gov.pr.idr.infra.livestock.mastitis;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisGateway;
import br.gov.pr.idr.domain.livestock.mastitis.MastitisID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.mastitis.persistence.MastitisJPAEntity;
import br.gov.pr.idr.infra.livestock.mastitis.persistence.MastitisJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MastitisPostgresGateway implements MastitisGateway {

    private final MastitisJPARepository repository;

    @Override
    public Mastitis save(final Mastitis mastitis) {
        return repository.save(MastitisJPAEntity.from(mastitis))
                .toAggregate();
    }

    @Override
    public Optional<Mastitis> findByIdAndAnimalId(final MastitisID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(MastitisJPAEntity::toAggregate);
    }

    @Override
    public Mastitis update(final Mastitis mastitis) {
        return this.save(mastitis);
    }

    @Override
    public void deleteById(final MastitisID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<Mastitis> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "diagnoseDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, MastitisJPAEntity::toAggregate);
    }
}
