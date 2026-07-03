package br.gov.pr.idr.infra.property_management.region;

import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegionPostgresGateway")
class RegionPostgresGatewayTest {

    @Mock RegionJPARepository repository;
    @Mock RegionJPAEntity entityMock;
    @InjectMocks RegionPostgresGateway gateway;

    private Region validRegion() {
        return Region.create("Região Sul");
    }

    @Test
    @DisplayName("deve salvar região e retornar agregado")
    void shouldSaveRegion() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validRegion());

        final var result = gateway.save(validRegion());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar região e retornar agregado")
    void shouldUpdateRegion() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validRegion());

        final var result = gateway.update(validRegion());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve verificar existência por descrição")
    void shouldCheckExistsByDescription() {
        when(repository.existsByDescription("Região Sul")).thenReturn(true);

        assertTrue(gateway.existsByDescription("Região Sul"));
    }

    @Test
    @DisplayName("deve retornar Optional com região quando encontrada por ID")
    void shouldFindByIdWhenFound() {
        final var id = RegionID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(validRegion());

        final var result = gateway.findByID(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando região não é encontrada")
    void shouldReturnEmptyWhenNotFound() {
        final var id = RegionID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findByID(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar região por ID")
    void shouldDeleteById() {
        final var id = RegionID.unique();

        gateway.deleteById(id);

        verify(repository).deleteById(id.id());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = RegionID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve retornar paginação de regiões na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(entityMock.toAggregate()).thenReturn(validRegion());

        final var query = SearchQuery.from(0, 10, "", "description", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }
}
