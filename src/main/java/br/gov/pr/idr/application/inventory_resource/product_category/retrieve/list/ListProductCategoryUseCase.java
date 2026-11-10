package br.gov.pr.idr.application.inventory_resource.product_category.retrieve.list;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

@QueryUseCase
public class ListProductCategoryUseCase extends UseCase<SearchQuery, Pagination<ListProductCategoryOutput>> {

    private final ProductCategoryGateway productCategoryGateway;

    public ListProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    @Override
    public Pagination<ListProductCategoryOutput> execute(final SearchQuery query) {
        return productCategoryGateway.search(query).map(ListProductCategoryOutput::from);
    }
}
