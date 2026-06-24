package br.gov.pr.idr.application.property_management.city;

import br.gov.pr.idr.application.property_management.city.retrieve.list.ListCityUseCase;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListCityUseCase")
class ListCityUseCaseTest {

    @Mock CityGateway cityGateway;
    @InjectMocks ListCityUseCase useCase;

    @Test
    @DisplayName("deve retornar paginação de cidades")
    void shouldReturnPaginatedCities() {
        final var result = new ListCityQueryResult(UUID.randomUUID(), "Curitiba",
                br.gov.pr.idr.domain.property_management.city.vo.State.PR, UUID.randomUUID(), "Região Sul");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(result));
        when(cityGateway.search(any())).thenReturn(pagination);

        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var output = useCase.execute(query);

        assertEquals(1L, output.total());
        assertEquals(1, output.items().size());
    }

    @Test
    @DisplayName("deve retornar paginação vazia quando não há cidades")
    void shouldReturnEmptyPagination() {
        final var empty = new Pagination<ListCityQueryResult>(0, 10, 0L, List.of());
        when(cityGateway.search(any())).thenReturn(empty);

        final var output = useCase.execute(SearchQuery.from(0, 10, "", "name", "asc"));

        assertTrue(output.items().isEmpty());
    }
}
