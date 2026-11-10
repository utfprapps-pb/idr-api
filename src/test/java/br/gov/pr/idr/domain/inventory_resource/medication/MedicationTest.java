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

@DisplayName("Medication — Aggregate")
class MedicationTest {

    @Test
    void shouldCreateWithValidFields() {
        final var medication = Medication.create(AnimalID.unique(), ProductID.unique(),
                LocalDate.of(2024, 1, 1), "10ml", "IM");
        assertNotNull(medication.getId());
        assertEquals("10ml", medication.getAppliedDose());
    }

    @Test
    void shouldFailWhenAppliedDoseIsBlank() {
        assertThrows(NotificationException.class, () -> Medication.create(AnimalID.unique(), ProductID.unique(),
                LocalDate.now(), " ", "IM"));
    }

    @Test
    void shouldFailWhenApplicationWayIsBlank() {
        assertThrows(NotificationException.class, () -> Medication.create(AnimalID.unique(), ProductID.unique(),
                LocalDate.now(), "10ml", " "));
    }

    @Test
    void shouldAllowNullApplicationDate() {
        final var medication = Medication.create(AnimalID.unique(), ProductID.unique(), null, "10ml", "IM");
        assertNull(medication.getApplicationDate());
    }
}
