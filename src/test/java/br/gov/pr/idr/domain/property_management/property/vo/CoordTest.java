package br.gov.pr.idr.domain.property_management.property.vo;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coord — Value Object")
class CoordTest {

    @Nested
    @DisplayName("Criação válida")
    class Valid {

        @Test
        @DisplayName("deve criar coordenada com latitude e longitude válidas")
        void shouldCreateWithValidValues() {
            final var lat = new BigDecimal("-25.4290");
            final var lon = new BigDecimal("-49.2671");

            final var coord = Coord.from(lat, lon);

            assertEquals(lat, coord.latitude());
            assertEquals(lon, coord.longitude());
        }

        @Test
        @DisplayName("deve criar coordenada com valores zero")
        void shouldCreateWithZeroValues() {
            assertDoesNotThrow(() -> Coord.from(BigDecimal.ZERO, BigDecimal.ZERO));
        }
    }

    @Nested
    @DisplayName("Criação inválida")
    class Invalid {

        @Test
        @DisplayName("deve rejeitar latitude nula")
        void shouldRejectNullLatitude() {
            assertThrows(DomainException.class,
                    () -> Coord.from(null, new BigDecimal("-49.2671")));
        }

        @Test
        @DisplayName("deve rejeitar longitude nula")
        void shouldRejectNullLongitude() {
            assertThrows(DomainException.class,
                    () -> Coord.from(new BigDecimal("-25.4290"), null));
        }

        @Test
        @DisplayName("deve rejeitar ambos nulos")
        void shouldRejectBothNull() {
            assertThrows(DomainException.class, () -> Coord.from(null, null));
        }
    }
}
