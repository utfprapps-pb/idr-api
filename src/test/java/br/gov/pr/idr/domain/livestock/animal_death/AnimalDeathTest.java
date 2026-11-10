package br.gov.pr.idr.domain.livestock.animal_death;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AnimalDeath — Aggregate")
class AnimalDeathTest {

    @Test
    void shouldCreateWithValidFields() {
        final var animalDeath = AnimalDeath.create(AnimalID.unique(), "Picada de cobra",
                LocalDate.of(2024, 1, 1));
        assertNotNull(animalDeath.getId());
        assertEquals("Picada de cobra", animalDeath.getReason());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> AnimalDeath.create(null, "Picada de cobra",
                LocalDate.now()));
    }
}
