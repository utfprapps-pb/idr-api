package br.gov.pr.idr.domain.livestock.animalsize;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnimalSize — Aggregate")
class AnimalSizeTest {

    @Test
    void shouldCreateWithValidSizeName() {
        final var animalSize = AnimalSize.create("Grande");
        assertNotNull(animalSize.getId());
        assertEquals("Grande", animalSize.getSizeName());
    }

    @Test
    void shouldFailWhenSizeNameIsNull() {
        assertThrows(NotificationException.class, () -> AnimalSize.create(null));
    }

    @Test
    void shouldFailWhenSizeNameIsBlank() {
        final var ex = assertThrows(NotificationException.class, () -> AnimalSize.create("  "));
        assertEquals("Nome do porte não pode ser vazio", ex.getErrors().get(0).message());
    }

    @Test
    void shouldFailWhenSizeNameIsEmpty() {
        assertThrows(NotificationException.class, () -> AnimalSize.create(""));
    }

    @Test
    void shouldAllowDuplicateSizeName() {
        final var a = AnimalSize.create("Grande");
        final var b = AnimalSize.create("Grande");
        assertNotEquals(a.getId(), b.getId());
    }
}
