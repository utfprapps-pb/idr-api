package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyUseCase;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.search.Pagination;
import br.gov.pr.idr.domain.shared.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchPropertyUseCase")
class SearchPropertyUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @InjectMocks SearchPropertyUseCase useCase;

    private Property stubProperty() {
        final var producerId = br.gov.pr.idr.domain.property_management.producer.ProducerID.from(UUID.randomUUID());
        final var cityId = br.gov.pr.idr.domain.property_management.city.CityID.from(UUID.randomUUID());
        return Property.with(
                PropertyID.unique(), "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                producerId, cityId, List.of(), List.of()
        );
    }

    @Test
    @DisplayName("deve retornar paginação de propriedades mapeada")
    void shouldReturnMappedPagination() {
        final var pagination = new Pagination<>(0, 10, 1L, List.of(stubProperty()));
        when(propertyGateway.search(any())).thenReturn(pagination);

        final var result = useCase.execute(SearchQuery.from(0, 10, "", "name", "asc"));

        assertEquals(1L, result.total());
        assertEquals(1, result.items().size());
        assertEquals("Fazenda", result.items().getFirst().name());
    }

    @Test
    @DisplayName("deve retornar paginação vazia quando não há propriedades")
    void shouldReturnEmptyPagination() {
        final var empty = new Pagination<Property>(0, 10, 0L, List.of());
        when(propertyGateway.search(any())).thenReturn(empty);

        final var result = useCase.execute(SearchQuery.from(0, 10, "", "name", "asc"));

        assertTrue(result.items().isEmpty());
    }
}
