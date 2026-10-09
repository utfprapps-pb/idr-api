package br.gov.pr.idr.infra.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.infra.inventory_resource.product.persistence.ProductJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Regression test: before the version-threading fix, update() threw
 * org.hibernate.NonUniqueObjectException against a real database, because
 * the JPA entity's @Version field was never populated on update, causing
 * Spring Data to treat it as a new entity (persist()) while a managed
 * instance with the same ID was already loaded in the same transaction
 * (findById(), called earlier in the same @Transactional Use Case method).
 * This test reproduces that exact call shape: save -> findById -> update
 * -> flush, all in one transaction, against a real (H2) persistence
 * context rather than a mocked repository.
 */
@DataJpaTest
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
