package br.gov.pr.idr.application.inventory_resource.product.retrieve.list;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

@QueryUseCase
public class ListProductUseCase extends UseCase<SearchQuery, Pagination<ListProductQueryResult>> {

    private final ProductGateway gateway;

    public ListProductUseCase(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Pagination<ListProductQueryResult> execute(final SearchQuery query) {
        return gateway.search(query);
    }
}
