package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

@QueryUseCase
public class SearchPropertyUseCase extends UseCase<SearchQuery, Pagination<SearchPropertyOutput>> {

    private final PropertyGateway propertyGateway;

    public SearchPropertyUseCase(final PropertyGateway propertyGateway) {
        this.propertyGateway = propertyGateway;
    }

    @Override
    public Pagination<SearchPropertyOutput> execute(final SearchQuery query) {
        return propertyGateway.search(query).map(SearchPropertyOutput::from);
    }
}
