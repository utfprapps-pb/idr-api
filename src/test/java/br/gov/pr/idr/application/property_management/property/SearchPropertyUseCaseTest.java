package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyCommand;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyUseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/** A resolução do escopo por papel é coberta em {@code PropertyAccessPolicyTest}; aqui o escopo chega no command. */
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchPropertyUseCase")
class SearchPropertyUseCaseTest {

    @Mock PropertyGateway propertyGateway;
    @InjectMocks SearchPropertyUseCase useCase;

    private static final SearchQuery QUERY = SearchQuery.from(0, 10, "", "name", "asc");

    @Test
    @DisplayName("busca com o escopo do command e devolve a paginação mapeada")
    void shouldSearchWithTheCommandScope() {
        final var scope = PropertySearchScope.byTechnician(UserID.unique());
        when(propertyGateway.search(QUERY, scope)).thenReturn(new Pagination<>(0, 10, 1L, List.of(stubProperty())));

        final var result = useCase.execute(SearchPropertyCommand.from(scope, QUERY));

        assertEquals(1L, result.total());
        assertEquals("Fazenda", result.items().getFirst().name());
    }

    private Property stubProperty() {
        return Property.with(
                PropertyID.unique(), "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                ProducerID.from(UUID.randomUUID()), CityID.from(UUID.randomUUID()), List.of(), List.of(),
                0L, Instant.now()
        );
    }
}
