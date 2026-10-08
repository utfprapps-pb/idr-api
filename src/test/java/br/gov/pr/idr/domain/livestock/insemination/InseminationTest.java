package br.gov.pr.idr.domain.livestock.insemination;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Insemination — Aggregate")
class InseminationTest {

    @Test
    void shouldCreateWithValidFields() {
        final var insemination = Insemination.create(AnimalID.unique(), LocalDate.of(2024, 1, 1), "Touro 1");
        assertNotNull(insemination.getId());
        assertEquals("Touro 1", insemination.getBull());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> Insemination.create(null, LocalDate.now(), "Touro 1"));
    }
}
