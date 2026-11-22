package br.gov.pr.idr.infra.shared.support;

import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

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
    @DisplayName("desempata pelo id depois do campo pedido, para a paginação não repetir nem pular registros")
    void shouldTieBreakById() {
        final var query = SearchQuery.from(1, 100, "", "identifier", "desc");

        final var orders = PageRequestFactory.from(query, "identifier").getSort().toList();

        assertEquals(2, orders.size());
        assertEquals("identifier", orders.get(0).getProperty());
        assertEquals(Sort.Direction.DESC, orders.get(0).getDirection());
        assertEquals("id", orders.get(1).getProperty());
    }

    @Test
    @DisplayName("ordenando pelo próprio id não repete o desempate")
    void shouldNotDuplicateIdSort() {
        final var query = SearchQuery.from(0, 10, "", "id", "asc");

        final var orders = PageRequestFactory.from(query, "name").getSort().toList();

        assertEquals(1, orders.size());
        assertEquals("id", orders.get(0).getProperty());
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

    @Test
    @DisplayName("dateTerm devolve a data quando terms é uma data ISO válida, ignorando espaços nas pontas")
    void shouldParseIsoDateTerm() {
        assertEquals(Optional.of(LocalDate.of(2024, 3, 10)), PageRequestFactory.dateTerm("2024-03-10"));
        assertEquals(Optional.of(LocalDate.of(2024, 3, 10)), PageRequestFactory.dateTerm("  2024-03-10 "));
    }

    @Test
    @DisplayName("dateTerm devolve vazio quando terms é nulo, em branco ou não é uma data ISO válida")
    void shouldReturnEmptyDateTermWhenNotIsoDate() {
        assertTrue(PageRequestFactory.dateTerm(null).isEmpty());
        assertTrue(PageRequestFactory.dateTerm("").isEmpty());
        assertTrue(PageRequestFactory.dateTerm("   ").isEmpty());
        assertTrue(PageRequestFactory.dateTerm("Touro").isEmpty());
        assertTrue(PageRequestFactory.dateTerm("10/03/2024").isEmpty());
        assertTrue(PageRequestFactory.dateTerm("2024-02-30").isEmpty());
        assertTrue(PageRequestFactory.dateTerm("2024-3-10").isEmpty());
    }
}
