package br.gov.pr.idr.infra.property_management.property;

import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPAEntity;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<Property> search(SearchQuery query, PropertySearchScope scope) {
        final var pageRequest = PageRequestFactory.from(query);
        final var terms = PageRequestFactory.terms(query.terms());

        final Page<PropertyJPAEntity> page = switch (scope.type()) {
            case UNRESTRICTED -> repository.search(terms, pageRequest);
            case BY_LOCATION -> scope.hasNoLocation()
                    ? Page.empty(pageRequest)
                    : repository.searchByLocation(terms, toUuids(scope.regionIds(), RegionID::id),
                            toUuids(scope.cityIds(), CityID::id), pageRequest);
            case BY_TECHNICIAN -> repository.searchByTechnician(terms, scope.technicianId().id(), pageRequest);
        };

        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getContent().stream().map(PropertyJPAEntity::toDomain).toList());
    }

    private static <T> Set<UUID> toUuids(final Set<T> ids, final Function<T, UUID> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }
}
