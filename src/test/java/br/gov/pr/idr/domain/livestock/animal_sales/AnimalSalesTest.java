package br.gov.pr.idr.domain.livestock.animal_sales;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AnimalSales — Aggregate")
class AnimalSalesTest {

    @Test
    void shouldCreateWithValidFields() {
        final var animalSales = AnimalSales.create(AnimalID.unique(), LocalDate.of(2024, 1, 1),
                Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));
        assertNotNull(animalSales.getId());
        assertEquals(BigDecimal.valueOf(1500), animalSales.getValue());
    }

    @Test
    void shouldFailWhenAnimalIdIsNull() {
        assertThrows(NotificationException.class, () -> AnimalSales.create(null, LocalDate.now(),
                Reason.VOLUNTARIA, BigDecimal.TEN, Destination.ABATE, BigDecimal.valueOf(450)));
    }

    @Test
    void shouldAllowNegativeValue() {
        // Fixa o comportamento atual do sistema legado: não há validação de value >= 0.
        // Isto é uma decisão deliberada (ver Review Focus da Task 6), não um descuido — não adicionar
        // validação aqui sem uma decisão explícita de produto.
        assertDoesNotThrow(() -> AnimalSales.create(AnimalID.unique(), LocalDate.now(), Reason.DESCARTE,
                BigDecimal.valueOf(-100), Destination.PRODUCAO, BigDecimal.valueOf(450)));
    }
}
