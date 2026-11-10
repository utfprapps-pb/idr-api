package br.gov.pr.idr.infra.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Teste de regressão: antes da correção que repassa a versão, update() lançava
 * org.hibernate.NonUniqueObjectException contra um banco real, porque o campo
 * {@code @Version} da entidade JPA nunca era preenchido no update. Com isso o
 * Spring Data tratava a entidade como nova (persist()) enquanto uma instância
 * gerenciada com o mesmo ID já estava carregada na mesma transação (o findById()
 * chamado antes, no mesmo método {@code @Transactional} do Use Case).
 * Este teste reproduz exatamente essa sequência: save -> findById -> update ->
 * flush, numa única transação, contra um contexto de persistência Postgres real
 * (Testcontainers) em vez de um repositório mockado.
 */
@PersistenceAdapterTest
class ProductUpdatePersistenceRegressionTest {

    @Autowired
    private ProductJPARepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("save -> findById -> update -> flush should not throw NonUniqueObjectException")
    void updateAfterFindByIdInSameTransactionShouldNotThrow() {
        final var gateway = new ProductPostgresGateway(repository);

        final var created = gateway.save(Product.create("Produto 1", "Desc 1", "Via 1",
                ActivePrincipleID.unique(), ProductCategoryID.unique()));

        final var loaded = gateway.findById(created.getId()).orElseThrow();

        final var updated = loaded.update("Produto 2", "Desc 2", "Via 2",
                loaded.getActivePrincipleId(), loaded.getCategoryId());

        assertDoesNotThrow(() -> {
            gateway.update(updated);
            entityManager.flush();
        });

        entityManager.clear();
        final var reloaded = gateway.findById(created.getId()).orElseThrow();
        assertEquals("Produto 2", reloaded.getName());
    }
}
