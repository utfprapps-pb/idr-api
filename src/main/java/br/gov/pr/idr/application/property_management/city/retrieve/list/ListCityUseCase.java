package br.gov.pr.idr.application.property_management.city.retrieve.list;

import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

@QueryUseCase
public class ListCityUseCase extends UseCase<SearchQuery, Pagination<ListCityQueryResult>> {

    private final CityGateway gateway;

    public ListCityUseCase(CityGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Pagination<ListCityQueryResult> execute(final SearchQuery query) {
        return gateway.search(query);
    }
}
