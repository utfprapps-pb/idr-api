package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;

public interface PropertyGateway {

    Property save(final Property property);

    Pagination<Property> search(final SearchQuery query);
}
