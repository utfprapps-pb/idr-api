package br.gov.pr.idr.domain.iam.user.query;

import br.gov.pr.idr.domain.shared.search.SearchQuery;

public record SearchUserQuery(SearchQuery query, boolean active) {

    public static SearchUserQuery from(
                               final int page,
                               final int perPage,
                               final String terms,
                               final String sort,
                               final String direction,
                               final Boolean active) {
        return new SearchUserQuery(SearchQuery.from(page, perPage, terms, sort, direction), active);
    }
}
