package br.gov.pr.idr.infra.inventory_resource.product_category;

import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.product_category.persistence.ProductCategoryJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.product_category.persistence.ProductCategoryJPARepository;
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
@DisplayName("ProductCategoryPostgresGateway")
class ProductCategoryPostgresGatewayTest {

    @Mock ProductCategoryJPARepository repository;
    @Mock ProductCategoryJPAEntity entityMock;
    @InjectMocks ProductCategoryPostgresGateway gateway;

    private ProductCategory validProductCategory() {
        return ProductCategory.create("Ração");
    }

    @Test
    @DisplayName("deve salvar categoria de produto e retornar agregado")
    void shouldSaveProductCategory() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validProductCategory());

        final var result = gateway.save(validProductCategory());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar categoria de produto e retornar agregado")
    void shouldUpdateProductCategory() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validProductCategory());

        final var result = gateway.update(validProductCategory());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com categoria de produto quando encontrada por ID")
    void shouldFindByIdWhenFound() {
        final var id = ProductCategoryID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(validProductCategory());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando categoria de produto não é encontrada")
    void shouldReturnEmptyWhenNotFound() {
        final var id = ProductCategoryID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve deletar categoria de produto por ID")
    void shouldDeleteById() {
        final var id = ProductCategoryID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = ProductCategoryID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve retornar paginação de categorias de produto na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(entityMock.toAggregate()).thenReturn(validProductCategory());

        final var query = SearchQuery.from(0, 10, "", "description", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }
}
