package br.gov.pr.idr.domain.inventory_resource.product_category;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductCategory — Aggregate")
class ProductCategoryTest {

    @Test
    void shouldCreateWithValidDescription() {
        final var category = ProductCategory.create("Antibiótico");
        assertNotNull(category.getId());
        assertEquals("Antibiótico", category.getDescription());
    }

    @Test
    void shouldFailWhenDescriptionIsNull() {
        assertThrows(NotificationException.class, () -> ProductCategory.create(null));
    }

    @Test
    void shouldFailWhenDescriptionIsBlank() {
        assertThrows(NotificationException.class, () -> ProductCategory.create("  "));
    }

    @Test
    void shouldAllowDuplicateDescription() {
        final var a = ProductCategory.create("Antibiótico");
        final var b = ProductCategory.create("Antibiótico");
        assertNotEquals(a.getId(), b.getId());
    }
}
