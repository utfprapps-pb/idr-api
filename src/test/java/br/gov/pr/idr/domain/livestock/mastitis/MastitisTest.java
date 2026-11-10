package br.gov.pr.idr.domain.livestock.mastitis;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Mastitis — Aggregate")
class MastitisTest {

    @Test
    void shouldCreateWithValidFields() {
        final var mastitis = Mastitis.create(AnimalID.unique(), LocalDate.of(2024, 1, 1), "Clinica", "+",
                "Positivo", "Negativo", "Positivo", "Negativo");
        assertNotNull(mastitis.getId());
        assertEquals("Clinica", mastitis.getMastitisType());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> Mastitis.create(null, LocalDate.now(), "Clinica", "+",
                "Positivo", "Negativo", "Positivo", "Negativo"));
    }
}
