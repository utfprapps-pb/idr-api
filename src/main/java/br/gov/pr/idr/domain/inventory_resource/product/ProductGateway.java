package br.gov.pr.idr.domain.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface ProductGateway {

    boolean existsById(final ProductID id);

    Product save(final Product product);

    Optional<Product> findById(final ProductID id);

    Product update(final Product product);

    Pagination<ListProductQueryResult> search(final SearchQuery query);

    void deleteById(final ProductID id);
}
