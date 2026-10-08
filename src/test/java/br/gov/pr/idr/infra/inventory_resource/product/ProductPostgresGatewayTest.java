package br.gov.pr.idr.infra.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPARepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductPostgresGateway")
class ProductPostgresGatewayTest {

    @Mock ProductJPARepository repository;
    @Mock ProductJPAEntity entityMock;
    @InjectMocks ProductPostgresGateway gateway;

    private Product validProduct() {
        return Product.create("Vacina X", "Vacina contra febre aftosa", "Intramuscular",
                ActivePrincipleID.unique(), ProductCategoryID.unique());
    }

    @Test
    @DisplayName("deve converter entidade JPA em agregado preservando activePrincipleId e categoryId (roundtrip)")
    void shouldRoundtripActivePrincipleAndCategoryIds() {
        final var product = validProduct();

        final var entity = ProductJPAEntity.from(product);
        final var aggregate = entity.toAggregate();

        assertEquals(product.getId(), aggregate.getId());
        assertEquals(product.getName(), aggregate.getName());
        assertEquals(product.getDescription(), aggregate.getDescription());
        assertEquals(product.getApplicationWay(), aggregate.getApplicationWay());
        assertEquals(product.getActivePrincipleId(), aggregate.getActivePrincipleId());
        assertEquals(product.getCategoryId(), aggregate.getCategoryId());
    }

    @Test
    @DisplayName("deve salvar produto e retornar agregado")
    void shouldSaveProduct() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validProduct());

        final var result = gateway.save(validProduct());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar produto e retornar agregado")
    void shouldUpdateProduct() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toAggregate()).thenReturn(validProduct());

        final var result = gateway.update(validProduct());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar Optional com produto quando encontrado por ID preservando referências de catálogo")
    void shouldFindByIdWhenFound() {
        final var id = ProductID.unique();
        final var product = validProduct();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toAggregate()).thenReturn(product);

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(product.getActivePrincipleId(), result.get().getActivePrincipleId());
        assertEquals(product.getCategoryId(), result.get().getCategoryId());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando não encontrado")
    void shouldReturnEmptyWhenNotFound() {
        final var id = ProductID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.empty());

        final var result = gateway.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = ProductID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve deletar produto por ID (soft delete)")
    void shouldDeleteById() {
        final var id = ProductID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));

        gateway.deleteById(id);

        verify(entityMock).markDeleted();
        verify(repository).save(entityMock);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deve retornar paginação na busca com nomes de ActivePrinciple/ProductCategory populados via join")
    void shouldReturnPaginationOnSearchWithJoinedNames() {
        final var productId = UUID.randomUUID();
        final var activePrincipleId = UUID.randomUUID();
        final var categoryId = UUID.randomUUID();
        final var queryResult = new ListProductQueryResult(
                productId, "Vacina X", "Vacina contra febre aftosa", "Intramuscular",
                activePrincipleId, "Aftosa Antígeno", categoryId, "Vacinas"
        );
        final var page = new PageImpl<>(List.of(queryResult));
        when(repository.search(any(), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
        final var item = result.items().get(0);
        assertEquals("Aftosa Antígeno", item.activePrinciple().name());
        assertEquals("Vacinas", item.category().description());
    }

    @Test
    @DisplayName("deve manter o produto na busca (LEFT JOIN) ainda que activePrincipleId/categoryId não casem com nenhuma linha")
    void shouldKeepProductRowWhenCatalogRefsDoNotMatchAnyRow() {
        final var productId = UUID.randomUUID();
        final var queryResult = new ListProductQueryResult(
                productId, "Vacina Órfã", "Descrição", "Oral",
                null, null, null, null
        );
        final var page = new PageImpl<>(List.of(queryResult));
        when(repository.search(any(), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "", "name", "asc");
        final var result = gateway.search(query);

        assertEquals(1, result.total());
        final var item = result.items().get(0);
        assertEquals(productId, item.id());
        assertNull(item.activePrinciple().id());
        assertNull(item.activePrinciple().name());
        assertNull(item.category().id());
        assertNull(item.category().description());
    }

    @Test
    @DisplayName("deve tratar terms nulo como string vazia na busca")
    void shouldTreatNullTermsAsEmptyOnSearch() {
        final var page = new PageImpl<ListProductQueryResult>(List.of());
        when(repository.search(eq(""), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, null, "name", "asc");
        final var result = gateway.search(query);

        assertEquals(0, result.total());
        verify(repository).search(eq(""), any(Pageable.class));
    }

    @Test
    @DisplayName("deve remover espaços em branco dos terms na busca")
    void shouldTrimTermsOnSearch() {
        final var page = new PageImpl<ListProductQueryResult>(List.of());
        when(repository.search(eq("Vacina"), any(Pageable.class))).thenReturn(page);

        final var query = SearchQuery.from(0, 10, "  Vacina  ", "name", "asc");
        gateway.search(query);

        verify(repository).search(eq("Vacina"), any(Pageable.class));
    }
}
