package br.gov.pr.idr.infra.shared.support;

import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class PageRequestFactory {

    private PageRequestFactory() {}

    public static PageRequest from(final SearchQuery query, final String defaultSort) {
        final var direction = "desc".equalsIgnoreCase(query.direction())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        final var sortField = query.sort() != null ? query.sort() : defaultSort;
        return PageRequest.of(query.page(), query.perPage(), Sort.by(direction, sortField));
    }

    public static PageRequest from(final SearchQuery query) {
        return from(query, "name");
    }

    public static PageRequest from(final SearchUserQuery query) {
        return from(query.query());
    }
}
