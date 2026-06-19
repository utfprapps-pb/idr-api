package br.gov.pr.idr.infra.property_management.property;

import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPAEntity;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PropertyPostgresGateway implements PropertyGateway {

    private final PropertyJPARepository repository;

    @Override
    public Property save(Property property) {
        return repository.save(PropertyJPAEntity.fromDomain(property))
                .toDomain();
    }

    @Override
    public Property update(Property property) {
        return this.save(property);
    }

    @Override
    public Optional<Property> findById(PropertyID id) {
        return repository.findById(id.id()).map(PropertyJPAEntity::toDomain);
    }

    @Override
    public Optional<GetPropertyQueryResult> findByIdWithDetails(PropertyID id) {
        return repository.findByIdWithDetails(id.id());
    }

    @Override
    public boolean existsById(PropertyID id) {
        return repository.existsById(id.id());
    }

    @Override
    public void deleteById(PropertyID id) {
        repository.deleteById(id.id());
    }

    @Override
    public Pagination<Property> search(SearchQuery query) {
        final var terms = query.terms() != null ? query.terms().trim() : "";
        final var pageRequest = PageRequestFactory.from(query);
        final var page = repository.search(terms, pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getContent().stream().map(PropertyJPAEntity::toDomain).toList());
    }
}
