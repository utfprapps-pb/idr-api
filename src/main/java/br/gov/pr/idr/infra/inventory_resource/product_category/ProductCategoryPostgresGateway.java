package br.gov.pr.idr.infra.inventory_resource.product_category;

import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.product_category.persistence.ProductCategoryJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.product_category.persistence.ProductCategoryJPARepository;
import br.gov.pr.idr.infra.inventory_resource.product_category.persistence.ProductCategorySpecification;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductCategoryPostgresGateway implements ProductCategoryGateway {

    private final ProductCategoryJPARepository repository;

    @Override
    public boolean existsById(final ProductCategoryID id) {
        return repository.existsById(id.id());
    }

    @Override
    public ProductCategory save(final ProductCategory category) {
        return repository.save(ProductCategoryJPAEntity.from(category))
                .toAggregate();
    }

    @Override
    public Optional<ProductCategory> findById(final ProductCategoryID id) {
        return repository.findById(id.id()).map(ProductCategoryJPAEntity::toAggregate);
    }

    @Override
    public ProductCategory update(final ProductCategory category) {
        return this.save(category);
    }

    @Override
    public Pagination<ProductCategory> search(final SearchQuery query) {
        final var terms = PageRequestFactory.terms(query.terms());
        final var pageRequest = PageRequestFactory.from(query, "description");
        final var page = repository.findAll(ProductCategorySpecification.withTerms(terms), pageRequest);
        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.map(ProductCategoryJPAEntity::toAggregate).toList());
    }

    @Override
    public void deleteById(final ProductCategoryID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }
}
