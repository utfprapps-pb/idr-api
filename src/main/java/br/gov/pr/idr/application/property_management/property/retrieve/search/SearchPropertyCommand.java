package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;

public record SearchPropertyCommand(
        String username,
        SearchQuery query
) {

    public static SearchPropertyCommand from(final String username, final SearchQuery query) {
        return new SearchPropertyCommand(username, query);
    }
}
