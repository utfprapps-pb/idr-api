package br.gov.pr.idr.domain.inventory_resource.medication;

import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Medication — Aggregate")
class MedicationTest {

    @Test
    void shouldCreateWithValidFields() {
        final var medication = Medication.create(AnimalID.unique(), ProductID.unique(), "Ivermectina 1%",
                "Ivermectina", LocalDate.of(2024, 1, 1), "10ml", "IM");
        assertNotNull(medication.getId());
        assertEquals("10ml", medication.getAppliedDose());
        assertEquals("Ivermectina 1%", medication.getProductName());
        assertEquals("Ivermectina", medication.getActivePrinciple());
    }

    @Test
    @DisplayName("aceita medicação sem produto do catálogo quando o nome do produto é informado")
    void shouldCreateWithoutCatalogProduct() {
        final var medication = Medication.create(AnimalID.unique(), null, "Vermífugo caseiro", null,
                LocalDate.of(2024, 1, 1), "10ml", "Oral");
        assertNull(medication.getProductId());
        assertEquals("Vermífugo caseiro", medication.getProductName());
        assertNull(medication.getActivePrinciple());
    }

    @Test
    @DisplayName("exige o nome do produto")
    void shouldFailWhenProductNameIsBlank() {
        final var exception = assertThrows(NotificationException.class, () -> Medication.create(AnimalID.unique(),
                null, " ", null, LocalDate.now(), "10ml", "IM"));
        assertTrue(exception.getErrors().stream()
                .anyMatch(error -> error.message().equals("O produto da medicação é obrigatório")));
    }

    @Test
    void shouldFailWhenAppliedDoseIsBlank() {
        assertThrows(NotificationException.class, () -> Medication.create(AnimalID.unique(), ProductID.unique(),
                "Produto", "Princípio", LocalDate.now(), " ", "IM"));
    }

    @Test
    void shouldFailWhenApplicationWayIsBlank() {
        assertThrows(NotificationException.class, () -> Medication.create(AnimalID.unique(), ProductID.unique(),
                "Produto", "Princípio", LocalDate.now(), "10ml", " "));
    }

    @Test
    void shouldAllowNullApplicationDate() {
        final var medication = Medication.create(AnimalID.unique(), ProductID.unique(), "Produto", "Princípio",
                null, "10ml", "IM");
        assertNull(medication.getApplicationDate());
    }

    @Test
    @DisplayName("update troca o produto e mantém a validação do nome")
    void shouldUpdateProductFields() {
        final var medication = Medication.create(AnimalID.unique(), ProductID.unique(), "Produto", "Princípio",
                null, "10ml", "IM");

        medication.update(null, "Outro produto", "Outro princípio", LocalDate.of(2024, 2, 2), "5ml", "SC");

        assertNull(medication.getProductId());
        assertEquals("Outro produto", medication.getProductName());
        assertEquals("Outro princípio", medication.getActivePrinciple());
        assertThrows(NotificationException.class,
                () -> medication.update(null, null, null, null, "5ml", "SC"));
    }
}
