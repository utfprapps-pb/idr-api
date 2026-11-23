package br.gov.pr.idr.infra.livestock.animalsize;

import br.gov.pr.idr.domain.livestock.animalsize.AnimalSize;
import br.gov.pr.idr.domain.livestock.animalsize.AnimalSizeID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animalsize.persistence.AnimalSizeJPAEntity;
import br.gov.pr.idr.infra.livestock.animalsize.persistence.AnimalSizeJPARepository;
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
@DisplayName("AnimalSizePostgresGateway")
class AnimalSizePostgresGatewayTest {

    @Mock AnimalSizeJPARepository repository;
    @Mock AnimalSizeJPAEntity entityMock;
    @InjectMocks AnimalSizePostgresGateway gateway;

    private AnimalSize validAnimalSize() {
        return AnimalSize.create("Grande");
    }

    @Test
    @DisplayName("deve salvar porte e retornar agregado")
    void shouldSaveAnimalSize() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalSize());

        final var result = gateway.save(validAnimalSize());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar porte e retornar agregado")
    void shouldUpdateAnimalSize() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validAnimalSize());

        final var result = gateway.update(validAnimalSize());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com porte quando encontrada por ID")
    void shouldFindByIdWhenFound() {
        final var id = AnimalSizeID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(validAnimalSize());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }


    @Test
    @DisplayName("deve retornar Optional vazio quando porte não é encontrada")
    void shouldReturnEmptyWhenNotFound() {
        final var id = AnimalSizeID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar porte por ID")
    void shouldDeleteById() {
        final var id = AnimalSizeID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = AnimalSizeID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve retornar paginação de portes na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(entityMock.toAggregate()).thenReturn(validAnimalSize());

        final var query = SearchQuery.from(0, 10, "", "sizeName", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }
}
