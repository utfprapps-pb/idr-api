package br.gov.pr.idr.domain.inventory_resource.active_principle;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ActivePrinciple — Aggregate")
class ActivePrincipleTest {

    @Test
    void shouldCreateWithValidName() {
        final var principle = ActivePrinciple.create("Amoxicilina");
        assertNotNull(principle.getId());
        assertEquals("Amoxicilina", principle.getName());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThrows(NotificationException.class, () -> ActivePrinciple.create(null));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(NotificationException.class, () -> ActivePrinciple.create("  "));
    }

    @Test
    void shouldAllowDuplicateName() {
        final var a = ActivePrinciple.create("Amoxicilina");
        final var b = ActivePrinciple.create("Amoxicilina");
        assertNotEquals(a.getId(), b.getId());
    }
}
