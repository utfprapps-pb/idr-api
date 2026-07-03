package br.gov.pr.idr.infra.property_management.city;

import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityPostgresGateway")
class CityPostgresGatewayTest {

    @Mock CityJPARepository repository;
    @Mock CityJPAEntity entityMock;
    @InjectMocks CityPostgresGateway gateway;

    private City validCity() {
        return City.create("Curitiba", State.PR, RegionID.unique());
    }

    @Test
    @DisplayName("deve salvar cidade e retornar agregado")
    void shouldSaveCity() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validCity());

        final var result = gateway.save(validCity());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar cidade e retornar agregado")
    void shouldUpdateCity() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validCity());

        final var result = gateway.update(validCity());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com cidade quando encontrada por ID")
    void shouldFindByIdWhenFound() {
        final var id = CityID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(validCity());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando não encontrada")
    void shouldReturnEmptyWhenNotFound() {
        final var id = CityID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = CityID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve deletar cidade por ID")
    void shouldDeleteById() {
        final var id = CityID.unique();

        gateway.deleteById(id);

        verify(repository).deleteById(id.id());
    }

    @Test
    @DisplayName("deve verificar existência por regionId")
    void shouldCheckExistsByRegionId() {
        final var regionId = RegionID.unique();
        when(repository.existsByRegionId(regionId.id())).thenReturn(false);

        assertFalse(gateway.existsByRegionId(regionId));
    }

    @Test
    @DisplayName("deve retornar paginação na busca")
    void shouldReturnPaginationOnSearch() {
        final var city = validCity();
        final var queryResult = new br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult(
                city.getId().id(), city.getName(), city.getState(), city.getRegionId().id(), "Região Sul"
        );
        final var page = new PageImpl<>(List.of(queryResult));
        when(repository.search(any(), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }

    @Test
    @DisplayName("deve tratar terms nulo como string vazia na busca")
    void shouldTreatNullTermsAsEmptyOnSearch() {
        final var page = new PageImpl<br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult>(List.of());
        when(repository.search(eq(""), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, null, "name", "asc");
        final var result = gateway.search(query);

        assertEquals(0, result.total());
        verify(repository).search(eq(""), any(Pageable.class));
    }

    @Test
    @DisplayName("deve remover espaços em branco dos terms na busca")
    void shouldTrimTermsOnSearch() {
        final var page = new PageImpl<br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult>(List.of());
        when(repository.search(eq("Curitiba"), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "  Curitiba  ", "name", "asc");
        gateway.search(query);

        verify(repository).search(eq("Curitiba"), any(Pageable.class));
    }
}
