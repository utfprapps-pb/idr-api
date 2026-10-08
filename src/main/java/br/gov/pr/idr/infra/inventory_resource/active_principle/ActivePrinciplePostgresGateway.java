package br.gov.pr.idr.infra.inventory_resource.active_principle;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.active_principle.persistence.ActivePrincipleJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.active_principle.persistence.ActivePrincipleJPARepository;
import br.gov.pr.idr.infra.inventory_resource.active_principle.persistence.ActivePrincipleSpecification;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ActivePrinciplePostgresGateway implements ActivePrincipleGateway {

    private final ActivePrincipleJPARepository repository;

    @Override
    public boolean existsById(final ActivePrincipleID id) {
        return repository.existsById(id.id());
    }

    @Override
    public ActivePrinciple save(final ActivePrinciple principle) {
        return repository.save(ActivePrincipleJPAEntity.from(principle))
                .toAggregate();
    }

    @Override
    public Optional<ActivePrinciple> findById(final ActivePrincipleID id) {
        return repository.findById(id.id()).map(ActivePrincipleJPAEntity::toAggregate);
    }

    @Override
    public ActivePrinciple update(final ActivePrinciple principle) {
        return this.save(principle);
    }

    @Override
    public Pagination<ActivePrinciple> search(final SearchQuery query) {
        final var terms = PageRequestFactory.terms(query.terms());
        final var pageRequest = PageRequestFactory.from(query, "name");
        final var page = repository.findAll(ActivePrincipleSpecification.withTerms(terms), pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.map(ActivePrincipleJPAEntity::toAggregate).toList());
    }

    @Override
    public void deleteById(final ActivePrincipleID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }
}
