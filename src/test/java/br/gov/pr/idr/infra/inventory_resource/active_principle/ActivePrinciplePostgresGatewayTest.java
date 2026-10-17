package br.gov.pr.idr.infra.inventory_resource.active_principle;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.active_principle.persistence.ActivePrincipleJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.active_principle.persistence.ActivePrincipleJPARepository;
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
@DisplayName("ActivePrinciplePostgresGateway")
class ActivePrinciplePostgresGatewayTest {

    @Mock ActivePrincipleJPARepository repository;
    @Mock ActivePrincipleJPAEntity entityMock;
    @InjectMocks ActivePrinciplePostgresGateway gateway;

    private ActivePrinciple validActivePrinciple() {
        return ActivePrinciple.create("Ivermectina");
    }

    @Test
    @DisplayName("deve salvar princípio ativo e retornar agregado")
    void shouldSaveActivePrinciple() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validActivePrinciple());

        final var result = gateway.save(validActivePrinciple());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar princípio ativo e retornar agregado")
    void shouldUpdateActivePrinciple() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validActivePrinciple());

        final var result = gateway.update(validActivePrinciple());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com princípio ativo quando encontrado por ID")
    void shouldFindByIdWhenFound() {
        final var id = ActivePrincipleID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(validActivePrinciple());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando princípio ativo não é encontrado")
    void shouldReturnEmptyWhenNotFound() {
        final var id = ActivePrincipleID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar princípio ativo por ID")
    void shouldDeleteById() {
        final var id = ActivePrincipleID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = ActivePrincipleID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve retornar paginação de princípios ativos na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(entityMock.toAggregate()).thenReturn(validActivePrinciple());

        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }
}
