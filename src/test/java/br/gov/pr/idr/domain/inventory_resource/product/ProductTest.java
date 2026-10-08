package br.gov.pr.idr.domain.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void shouldCreateWithValidFields() {
        final var product = Product.create("Produto 1", "Desc", "Via 1",
                ActivePrincipleID.unique(), ProductCategoryID.unique());
        assertNotNull(product.getId());
        assertEquals("Produto 1", product.getName());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(NotificationException.class, () -> Product.create("", "Desc", "Via 1",
                ActivePrincipleID.unique(), ProductCategoryID.unique()));
    }

    @Test
    void shouldFailWhenActivePrincipleIdIsNull() {
        assertThrows(NotificationException.class, () -> Product.create("Produto 1", "Desc", "Via 1",
                null, ProductCategoryID.unique()));
    }

    @Test
    void shouldFailWhenCategoryIdIsNull() {
        assertThrows(NotificationException.class, () -> Product.create("Produto 1", "Desc", "Via 1",
                ActivePrincipleID.unique(), null));
    }
}
