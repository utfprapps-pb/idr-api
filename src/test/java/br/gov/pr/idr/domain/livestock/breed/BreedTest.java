package br.gov.pr.idr.domain.livestock.breed;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Breed — Aggregate")
class BreedTest {

    @Test
    void shouldCreateWithValidBreedName() {
        final var breed = Breed.create("Nelore");
        assertNotNull(breed.getId());
        assertEquals("Nelore", breed.getBreedName());
    }

    @Test
    void shouldFailWhenBreedNameIsNull() {
        assertThrows(NotificationException.class, () -> Breed.create(null));
    }

    @Test
    void shouldFailWhenBreedNameIsBlank() {
        assertThrows(NotificationException.class, () -> Breed.create("  "));
    }

    @Test
    void shouldAllowDuplicateBreedName() {
        final var a = Breed.create("Nelore");
        final var b = Breed.create("Nelore");
        assertNotEquals(a.getId(), b.getId());
    }
}
