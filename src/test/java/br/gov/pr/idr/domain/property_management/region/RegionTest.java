package br.gov.pr.idr.domain.property_management.region;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Region — Aggregate")
class RegionTest {

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar região com descrição válida")
        void shouldCreateValidRegion() {
            final var region = Region.create("Região Sul");

            assertNotNull(region.getId());
            assertEquals("Região Sul", region.getDescription());
        }

        @Test
        @DisplayName("deve rejeitar região sem descrição")
        void shouldRejectNullDescription() {
            final var ex = assertThrows(NotificationException.class, () -> Region.create(null));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Descrição da região")));
        }

        @Test
        @DisplayName("deve rejeitar região com descrição em branco")
        void shouldRejectBlankDescription() {
            assertThrows(NotificationException.class, () -> Region.create("   "));
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir região a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = RegionID.unique();
            final var region = Region.with(id, "Região Norte");

            assertEquals(id, region.getId());
            assertEquals("Região Norte", region.getDescription());
        }
    }

    @Nested
    @DisplayName("Atualização")
    class Update {

        @Test
        @DisplayName("deve atualizar descrição da região")
        void shouldUpdateDescription() {
            final var region = Region.create("Região Sul");
            region.update("Região Sul Atualizada");

            assertEquals("Região Sul Atualizada", region.getDescription());
        }

        @Test
        @DisplayName("deve rejeitar atualização com descrição nula")
        void shouldRejectUpdateWithNullDescription() {
            final var region = Region.create("Região Sul");
            assertThrows(NotificationException.class, () -> region.update(null));
        }

        @Test
        @DisplayName("deve rejeitar atualização com descrição em branco")
        void shouldRejectUpdateWithBlankDescription() {
            final var region = Region.create("Região Sul");
            assertThrows(NotificationException.class, () -> region.update(""));
        }
    }
}
