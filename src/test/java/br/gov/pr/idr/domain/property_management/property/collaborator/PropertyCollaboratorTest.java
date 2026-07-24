package br.gov.pr.idr.domain.property_management.property.collaborator;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PropertyCollaborator — Entity")
class PropertyCollaboratorTest {

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar colaborador com dados válidos")
        void shouldCreateValidCollaborator() {
            final var collaborator = PropertyCollaborator.create("Pedro Ferreira", "8");

            assertNotNull(collaborator.getId());
            assertEquals("Pedro Ferreira", collaborator.getName());
            assertEquals("8", collaborator.getHoursPerDay());
        }

        @Test
        @DisplayName("deve rejeitar colaborador sem nome")
        void shouldRejectNullName() {
            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyCollaborator.create(null, "8"));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome do trabalhador")));
        }

        @Test
        @DisplayName("deve rejeitar colaborador com nome em branco")
        void shouldRejectBlankName() {
            assertThrows(NotificationException.class, () -> PropertyCollaborator.create("  ", "8"));
        }

        @Test
        @DisplayName("deve rejeitar colaborador sem horas por dia")
        void shouldRejectNullHours() {
            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyCollaborator.create("Pedro", null));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Horas de trabalho")));
        }

        @Test
        @DisplayName("deve rejeitar colaborador com horas em branco")
        void shouldRejectBlankHours() {
            assertThrows(NotificationException.class, () -> PropertyCollaborator.create("Pedro", "  "));
        }

        @Test
        @DisplayName("deve acumular ambos os erros quando nome e horas são nulos")
        void shouldAccumulateBothErrors() {
            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyCollaborator.create(null, null));
            assertEquals(2, ex.getErrors().size());
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir colaborador a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = PropertyCollaboratorID.unique();
            final var collaborator = PropertyCollaborator.with(id, "Ana Lima", "6");

            assertEquals(id, collaborator.getId());
            assertEquals("Ana Lima", collaborator.getName());
            assertEquals("6", collaborator.getHoursPerDay());
        }
    }
}
