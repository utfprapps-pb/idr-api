package br.gov.pr.idr.infra.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPARepository;
import br.gov.pr.idr.infra.shared.persistence.LikeSpecification;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class ProductPostgresGateway implements ProductGateway {

    private final ProductJPARepository repository;

    @Override
    public boolean existsById(final ProductID id) {
        return repository.existsById(id.id());
    }

    @Override
    public Product save(final Product product) {
        return repository.save(ProductJPAEntity.from(product))
                .toAggregate();
    }

    @Override
    public Optional<Product> findById(final ProductID id) {
        return repository.findById(id.id())
                .map(ProductJPAEntity::toAggregate);
    }

    @Override
    public Product update(final Product product) {
        return this.save(product);
    }

    @Override
    public Pagination<ListProductQueryResult> search(final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query);
        final var pattern = LikeSpecification.containsPattern(PageRequestFactory.terms(query.terms()));
        return PaginationFactory.from(repository.search(pattern, pageRequest), Function.identity());
    }

    @Override
    public void deleteById(final ProductID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }
}
