package br.gov.pr.idr.domain.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PregnancyDiagnose — Aggregate")
class PregnancyDiagnoseTest {

    @Test
    void shouldCreateWithValidFields() {
        final var pregnancyDiagnose = PregnancyDiagnose.create(AnimalID.unique(), LocalDate.of(2024, 1, 1),
                LocalDate.of(2023, 11, 1));
        assertNotNull(pregnancyDiagnose.getId());
        assertEquals(LocalDate.of(2023, 11, 1), pregnancyDiagnose.getLastIa());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> PregnancyDiagnose.create(null, LocalDate.now(),
                LocalDate.now()));
    }
}
