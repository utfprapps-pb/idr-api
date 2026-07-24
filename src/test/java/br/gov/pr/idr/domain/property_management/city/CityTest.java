package br.gov.pr.idr.domain.property_management.city;

import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("City — Aggregate")
class CityTest {

    private static final State STATE = State.PR;
    private static final RegionID REGION_ID = RegionID.unique();

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar cidade com dados válidos")
        void shouldCreateValidCity() {
            final var city = City.create("Curitiba", STATE, REGION_ID);

            assertNotNull(city.getId());
            assertEquals("Curitiba", city.getName());
            assertEquals(STATE, city.getState());
            assertEquals(REGION_ID, city.getRegionId());
        }

        @Test
        @DisplayName("deve rejeitar cidade sem nome")
        void shouldRejectNullName() {
            final var ex = assertThrows(NotificationException.class,
                    () -> City.create(null, STATE, REGION_ID));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("nome")));
        }

        @Test
        @DisplayName("deve rejeitar cidade com nome em branco")
        void shouldRejectBlankName() {
            assertThrows(NotificationException.class, () -> City.create("  ", STATE, REGION_ID));
        }

        @Test
        @DisplayName("deve rejeitar cidade sem estado")
        void shouldRejectNullState() {
            final var ex = assertThrows(NotificationException.class,
                    () -> City.create("Curitiba", null, REGION_ID));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("estado")));
        }

        @Test
        @DisplayName("deve rejeitar cidade sem região")
        void shouldRejectNullRegion() {
            final var ex = assertThrows(NotificationException.class,
                    () -> City.create("Curitiba", STATE, null));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("região")));
        }

        @Test
        @DisplayName("deve acumular múltiplos erros")
        void shouldAccumulateErrors() {
            final var ex = assertThrows(NotificationException.class,
                    () -> City.create(null, null, null));
            assertTrue(ex.getErrors().size() >= 3);
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir cidade com ID existente")
        void shouldReconstitute() {
            final var id = CityID.unique();
            final var city = City.with(id, "Londrina", State.PR, REGION_ID);

            assertEquals(id, city.getId());
            assertEquals("Londrina", city.getName());
        }
    }

    @Nested
    @DisplayName("Atualização")
    class Update {

        @Test
        @DisplayName("deve atualizar nome e região")
        void shouldUpdateNameAndRegion() {
            final var city = City.create("Curitiba", STATE, REGION_ID);
            final var newRegion = RegionID.unique();

            city.update(newRegion, "Pinhais");

            assertEquals("Pinhais", city.getName());
            assertEquals(newRegion, city.getRegionId());
        }

        @Test
        @DisplayName("deve rejeitar atualização com nome nulo")
        void shouldRejectUpdateWithNullName() {
            final var city = City.create("Curitiba", STATE, REGION_ID);
            assertThrows(NotificationException.class, () -> city.update(REGION_ID, null));
        }
    }
}
