package br.gov.pr.idr.infra.livestock.breed;

import br.gov.pr.idr.domain.livestock.breed.Breed;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPAEntity;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository;
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
@DisplayName("BreedPostgresGateway")
class BreedPostgresGatewayTest {

    @Mock BreedJPARepository repository;
    @Mock BreedJPAEntity entityMock;
    @InjectMocks BreedPostgresGateway gateway;

    private Breed validBreed() {
        return Breed.create("Nelore");
    }

    @Test
    @DisplayName("deve salvar raça e retornar agregado")
    void shouldSaveBreed() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validBreed());

        final var result = gateway.save(validBreed());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar raça e retornar agregado")
    void shouldUpdateBreed() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validBreed());

        final var result = gateway.update(validBreed());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com raça quando encontrada por ID")
    void shouldFindByIdWhenFound() {
        final var id = BreedID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(validBreed());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }


    @Test
    @DisplayName("deve retornar Optional vazio quando raça não é encontrada")
    void shouldReturnEmptyWhenNotFound() {
        final var id = BreedID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar raça por ID")
    void shouldDeleteById() {
        final var id = BreedID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = BreedID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve retornar paginação de raças na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(entityMock.toAggregate()).thenReturn(validBreed());

        final var query = SearchQuery.from(0, 10, "", "breedName", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }
}
