package br.gov.pr.idr.domain.livestock.animal_diseases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AnimalDiseases — Aggregate")
class AnimalDiseasesTest {

    @Test
    void shouldCreateWithValidFields() {
        final var animalDiseases = AnimalDiseases.create(AnimalID.unique(), "Febre aftosa",
                LocalDate.of(2024, 1, 1));
        assertNotNull(animalDiseases.getId());
        assertEquals("Febre aftosa", animalDiseases.getDiagnosis());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> AnimalDiseases.create(null, "Febre aftosa",
                LocalDate.now()));
    }
}
