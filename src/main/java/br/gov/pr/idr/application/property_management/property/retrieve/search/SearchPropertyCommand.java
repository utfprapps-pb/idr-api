package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

public record SearchPropertyCommand(
        PropertySearchScope scope,
        SearchQuery query
) {

    public static SearchPropertyCommand from(final PropertySearchScope scope, final SearchQuery query) {
        return new SearchPropertyCommand(scope, query);
    }
}
