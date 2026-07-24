package br.gov.pr.idr.infra.property_management.region;

import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionSpecification;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RegionPostgresGateway implements RegionGateway {

    private final RegionJPARepository repository;

    @Override
    public boolean existsById(RegionID id) {
        return repository.existsById(id.id());
    }

    @Override
    public Region save(final Region region) {
        return repository.save(RegionJPAEntity.from(region))
                .toAggregate();
    }

    @Override
    public boolean existsByDescription(final String description) {
        return repository.existsByDescription(description);
    }

    @Override
    public Optional<Region> findByID(final RegionID id) {
        return repository.findById(id.id()).map(RegionJPAEntity::toAggregate);
    }

    @Override
    public Region update(final Region region) {
        return this.save(region);
    }

    @Override
    public Pagination<Region> search(SearchQuery query) {
        final var terms = PageRequestFactory.terms(query.terms());
        final var pageRequest = PageRequestFactory.from(query, "description");
        final var page = repository.findAll(RegionSpecification.withTerms(terms), pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.map(RegionJPAEntity::toAggregate).toList());
    }

    @Override
    public void deleteById(final RegionID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }
}
