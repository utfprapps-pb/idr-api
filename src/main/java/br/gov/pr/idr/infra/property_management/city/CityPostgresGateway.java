package br.gov.pr.idr.infra.property_management.city;

import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CityPostgresGateway implements CityGateway {

    private final CityJPARepository repository;

    @Override
    public boolean existsById(final CityID id) {
        return repository.existsById(id.id());
    }

    @Override
    public City save(City city) {
        return repository.save(CityJPAEntity.from(city))
                .toAggregate();
    }

    @Override
    public Optional<City> findById(CityID cityId) {
        return repository.findById(cityId.id())
                .map(CityJPAEntity::toAggregate);
    }

    @Override
    public City update(City city) {
        return this.save(city);
    }

    @Override
    public Pagination<ListCityQueryResult> search(SearchQuery query) {
        final var terms = query.terms() != null && !query.terms().isBlank() ? query.terms() : "";
        final var direction = "desc".equalsIgnoreCase(query.direction())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        final var sort = Sort.by(direction, query.sort());
        var pageRequest = PageRequest.of(query.page(), query.perPage(), sort);
        final var page = repository.search(terms, pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(), page.getContent());
    }
}
