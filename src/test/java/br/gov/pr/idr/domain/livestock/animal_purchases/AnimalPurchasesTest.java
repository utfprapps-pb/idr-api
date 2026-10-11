package br.gov.pr.idr.domain.livestock.animal_purchases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AnimalPurchases — Aggregate")
class AnimalPurchasesTest {

    @Test
    void shouldCreateWithValidFields() {
        final var animalPurchases = AnimalPurchases.create(AnimalID.unique(), LocalDate.of(2024, 1, 1),
                LocalDate.of(2023, 1, 1), BigDecimal.valueOf(1500), BigDecimal.valueOf(450));
        assertNotNull(animalPurchases.getId());
        assertEquals(BigDecimal.valueOf(1500), animalPurchases.getValue());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> AnimalPurchases.create(null, LocalDate.now(),
                LocalDate.now(), BigDecimal.TEN, BigDecimal.valueOf(450)));
    }

    @Test
    void shouldAllowNegativeValue() {
        // Como no legado, value não é validado; mudar isso exige decisão de produto.
        assertDoesNotThrow(() -> AnimalPurchases.create(AnimalID.unique(), LocalDate.now(), LocalDate.now(),
                BigDecimal.valueOf(-100), BigDecimal.valueOf(450)));
    }
}
