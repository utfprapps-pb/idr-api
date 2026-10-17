package br.gov.pr.idr.domain.livestock.animal;

import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Animal — Aggregate")
class AnimalTest {

    @Test
    void shouldCreateWithValidFields() {
        final var animal = Animal.create(PropertyID.unique(), null, BreedID.unique(), "Leiteiro", "Mimosa",
                "F", "Vivo", LocalDate.of(2024, 1, 1), 30f, 40f, 50f, 3.5f);
        assertNotNull(animal.getId());
        assertEquals("Mimosa", animal.getIdentifier());
        assertTrue(animal.getAnimalMotherId().isEmpty());
    }

    @Test
    void shouldFailWhenPropertyIdIsNull() {
        assertThrows(NotificationException.class, () -> Animal.create(null, null, BreedID.unique(), "T",
                "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f));
    }

    @Test
    void shouldFailWhenBreedIdIsNull() {
        assertThrows(NotificationException.class, () -> Animal.create(PropertyID.unique(), null, null, "T",
                "I", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f));
    }

    @Test
    void shouldFailWhenIdentifierIsBlank() {
        assertThrows(NotificationException.class, () -> Animal.create(PropertyID.unique(), null, BreedID.unique(), "T",
                " ", "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f));
    }

    @Test
    void updateShouldNotChangePropertyId() {
        final var original = Animal.create(PropertyID.unique(), null, BreedID.unique(), "T", "I",
                "F", "Vivo", LocalDate.now(), 1f, 1f, 1f, 1f);
        final var propertyIdBefore = original.getPropertyId();

        original.update(null, BreedID.unique(), "T2", "I2", "M", "Morto", LocalDate.now(), 2f, 2f, 2f, 2f);

        assertEquals(propertyIdBefore, original.getPropertyId());
    }
}
