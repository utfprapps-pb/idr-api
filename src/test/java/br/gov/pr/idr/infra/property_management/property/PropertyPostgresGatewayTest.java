package br.gov.pr.idr.infra.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.property.Property;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.property_management.property.vo.Coord;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPAEntity;
import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PropertyPostgresGateway")
class PropertyPostgresGatewayTest {

    @Mock PropertyJPARepository repository;
    @Mock PropertyJPAEntity entityMock;
    @InjectMocks PropertyPostgresGateway gateway;

    private Property validProperty() {
        return Property.with(
                PropertyID.unique(), "Fazenda",
                Coord.from(new BigDecimal("-25.43"), new BigDecimal("-49.27")),
                BigDecimal.ZERO, BigDecimal.ZERO, 0.0, 0.0, 0.0, 0.0,
                ProducerID.from(UUID.randomUUID()),
                CityID.from(UUID.randomUUID()),
                List.of(), List.of(), null, null
        );
    }

    @Test
    @DisplayName("deve salvar propriedade e retornar domínio")
    void shouldSaveProperty() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(validProperty());

        final var result = gateway.save(validProperty());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar propriedade delegando para save")
    void shouldUpdateProperty() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(validProperty());

        final var result = gateway.update(validProperty());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com propriedade quando encontrada por ID")
    void shouldFindByIdWhenFound() {
        final var id = PropertyID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toDomain()).thenReturn(validProperty());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando não encontrada")
    void shouldReturnEmptyWhenNotFound() {
        final var id = PropertyID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar resultado detalhado por ID")
    void shouldFindByIdWithDetails() {
        final var id = PropertyID.unique();
        final var queryResult = new GetPropertyQueryResult(
                id.id(), "Fazenda", null, null, 0.0, 0.0, 0.0, 0.0,
                null, null, null, null, UUID.randomUUID(), "Produtor",
                UUID.randomUUID(), "Curitiba", null, null, null
        );
        when(repository.findByIdWithDetails(id.id())).thenReturn(Optional.of(queryResult));

        final var result = gateway.findByIdWithDetails(id);

        assertTrue(result.isPresent());
        assertEquals("Fazenda", result.get().name());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = PropertyID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve deletar propriedade por ID")
    void shouldDeleteById() {
        final var id = PropertyID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve retornar paginação na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        final var userID = UserID.unique();
        when(repository.searchByTechnician(any(), eq(userID.id()), any(Pageable.class))).thenReturn(page);
        when(entityMock.toDomain()).thenReturn(validProperty());
        final var scope = PropertySearchScope.byTechnician(userID);
        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var result = gateway.search(query, scope);

        assertEquals(1, result.total());
    }

    @Test
    @DisplayName("deve buscar sem restrição de escopo")
    void shouldReturnPaginationOnUnrestrictedSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.search(any(), any(Pageable.class))).thenReturn(page);
        when(entityMock.toDomain()).thenReturn(validProperty());
        final var scope = PropertySearchScope.unrestricted();
        final var query = SearchQuery.from(0, 10, "", "name", "asc");

        final var result = gateway.search(query, scope);

        assertEquals(1, result.total());
        verify(repository).search(any(), any(Pageable.class));
    }

    @Test
    @DisplayName("deve buscar por localização quando região ou cidade informadas")
    void shouldReturnPaginationOnSearchByLocation() {
        final var page = new PageImpl<>(List.of(entityMock));
        final var regionId = RegionID.unique();
        final var cityId = CityID.unique();
        when(repository.searchByLocation(any(), eq(Set.of(regionId.id())), eq(Set.of(cityId.id())), any(Pageable.class)))
                .thenReturn(page);
        when(entityMock.toDomain()).thenReturn(validProperty());
        final var scope = PropertySearchScope.byLocation(Set.of(regionId), Set.of(cityId));
        final var query = SearchQuery.from(0, 10, "", "name", "asc");

        final var result = gateway.search(query, scope);

        assertEquals(1, result.total());
        verify(repository).searchByLocation(any(), eq(Set.of(regionId.id())), eq(Set.of(cityId.id())), any(Pageable.class));
    }

    @Test
    @DisplayName("deve retornar página vazia quando escopo por localização não possui região ou cidade")
    void shouldReturnEmptyPageWhenSearchByLocationHasNoLocation() {
        final var scope = PropertySearchScope.byLocation(Set.of(), Set.of());
        final var query = SearchQuery.from(0, 10, "", "name", "asc");

        final var result = gateway.search(query, scope);

        assertEquals(0, result.total());
        verify(repository, never()).searchByLocation(any(), any(), any(), any());
    }
}
