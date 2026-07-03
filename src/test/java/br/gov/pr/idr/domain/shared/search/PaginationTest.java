package br.gov.pr.idr.domain.shared.search;

import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pagination — Record")
class PaginationTest {

    @Test
    @DisplayName("deve armazenar dados de paginação corretamente")
    void shouldStoreAllPaginationData() {
        final var items = List.of("a", "b", "c");
        final var pagination = new Pagination<>(0, 10, 3L, items);

        assertEquals(0, pagination.currentPage());
        assertEquals(10, pagination.perPage());
        assertEquals(3L, pagination.total());
        assertEquals(items, pagination.items());
    }

    @Test
    @DisplayName("deve mapear items usando a função fornecida")
    void shouldMapItems() {
        final var pagination = new Pagination<>(0, 10, 3L, List.of(1, 2, 3));

        final var mapped = pagination.map(Object::toString);

        assertEquals(List.of("1", "2", "3"), mapped.items());
        assertEquals(0, mapped.currentPage());
        assertEquals(10, mapped.perPage());
        assertEquals(3L, mapped.total());
    }

    @Test
    @DisplayName("deve preservar metadados de paginação ao mapear")
    void shouldPreservePaginationMetadataOnMap() {
        final var pagination = new Pagination<>(2, 5, 100L, List.of("x"));
        final var mapped = pagination.map(String::toUpperCase);

        assertEquals(2, mapped.currentPage());
        assertEquals(5, mapped.perPage());
        assertEquals(100L, mapped.total());
    }

    @Test
    @DisplayName("deve mapear lista vazia sem erros")
    void shouldHandleEmptyList() {
        final var pagination = new Pagination<>(0, 10, 0L, List.<String>of());
        final var mapped = pagination.map(String::length);

        assertTrue(mapped.items().isEmpty());
    }
}
