package br.gov.pr.idr.domain.inventory_resource.product_category;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

import java.util.Optional;

public interface ProductCategoryGateway {

    boolean existsById(final ProductCategoryID id);

    ProductCategory save(final ProductCategory category);

    Optional<ProductCategory> findById(final ProductCategoryID id);

    ProductCategory update(final ProductCategory category);

    Pagination<ProductCategory> search(final SearchQuery query);

    void deleteById(final ProductCategoryID id);
}
