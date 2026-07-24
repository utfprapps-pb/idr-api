package br.gov.pr.idr.infra.shared.support;

import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PageRequestFactory")
class PageRequestFactoryTest {

    @Test
    @DisplayName("deve criar PageRequest com ordenação ascendente")
    void shouldCreatePageRequestWithAscSort() {
        final var query = SearchQuery.from(0, 10, "", "name", "asc");

        final var pageRequest = PageRequestFactory.from(query);

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals(Sort.Direction.ASC, pageRequest.getSort().getOrderFor("name").getDirection());
    }

    @Test
    @DisplayName("deve criar PageRequest com ordenação descendente")
    void shouldCreatePageRequestWithDescSort() {
        final var query = SearchQuery.from(1, 5, "termo", "description", "desc");

        final var pageRequest = PageRequestFactory.from(query, "description");

        assertEquals(1, pageRequest.getPageNumber());
        assertEquals(5, pageRequest.getPageSize());
        assertEquals(Sort.Direction.DESC, pageRequest.getSort().getOrderFor("description").getDirection());
    }

    @Test
    @DisplayName("deve usar campo padrão quando sort é nulo")
    void shouldUseDefaultSortWhenSortIsNull() {
        final var query = SearchQuery.from(0, 10, "", null, "asc");

        final var pageRequest = PageRequestFactory.from(query, "description");

        assertNotNull(pageRequest.getSort().getOrderFor("description"));
    }

    @Test
    @DisplayName("deve criar PageRequest a partir de SearchUserQuery")
    void shouldCreateFromSearchUserQuery() {
        final var userQuery = SearchUserQuery.from(0, 10, "", "name", "asc", null);

        final var pageRequest = PageRequestFactory.from(userQuery);

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
    }
}
