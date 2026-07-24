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

        @Test
        @DisplayName("deve rejeitar latitude menor que -90")
        void shouldRejectLatitudeBelowMinimum() {
            assertThrows(DomainException.class,
                    () -> Coord.from(new BigDecimal("-90.1"), BigDecimal.ZERO));
        }

        @Test
        @DisplayName("deve rejeitar latitude maior que 90")
        void shouldRejectLatitudeAboveMaximum() {
            assertThrows(DomainException.class,
                    () -> Coord.from(new BigDecimal("90.1"), BigDecimal.ZERO));
        }

        @Test
        @DisplayName("deve rejeitar longitude menor que -180")
        void shouldRejectLongitudeBelowMinimum() {
            assertThrows(DomainException.class,
                    () -> Coord.from(BigDecimal.ZERO, new BigDecimal("-180.1")));
        }

        @Test
        @DisplayName("deve rejeitar longitude maior que 180")
        void shouldRejectLongitudeAboveMaximum() {
            assertThrows(DomainException.class,
                    () -> Coord.from(BigDecimal.ZERO, new BigDecimal("180.1")));
        }
    }
}
