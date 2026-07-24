package br.gov.pr.idr.infra.property_management.region.models.list;

import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListRegionUseCase")
class ListRegionUseCaseTest {

    @Mock RegionGateway regionGateway;
    @InjectMocks ListRegionUseCase useCase;

    @Test
    @DisplayName("deve retornar paginação mapeada para ListRegionOutput")
    void shouldReturnMappedPagination() {
        final var region = Region.create("Região Sul");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(region));
        when(regionGateway.search(any())).thenReturn(pagination);

        final var result = useCase.execute(SearchQuery.from(0, 10, "", "description", "asc"));

        assertEquals(1L, result.total());
        assertEquals(1, result.items().size());
        assertEquals(region.getId().id(), result.items().getFirst().id());
        assertEquals("Região Sul", result.items().getFirst().description());
    }

    @Test
    @DisplayName("deve retornar paginação vazia quando não há regiões")
    void shouldReturnEmptyPagination() {
        final var empty = new Pagination<Region>(0, 10, 0L, List.of());
        when(regionGateway.search(any())).thenReturn(empty);

        final var result = useCase.execute(SearchQuery.from(0, 10, "", "description", "asc"));

        assertTrue(result.items().isEmpty());
    }
}
