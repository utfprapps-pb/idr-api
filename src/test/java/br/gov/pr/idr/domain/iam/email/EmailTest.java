package br.gov.pr.idr.domain.iam.email;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email — Aggregate (Recuperação de senha)")
class EmailTest {

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar email com código de 8 caracteres e expiração em 5 minutos")
        void shouldCreateWithCodeAndExpiration() {
            final var email = Email.create("user@example.com", "joao");

            assertNotNull(email.getId());
            assertNotNull(email.getCode());
            assertEquals(8, email.getCode().length());
            assertEquals("user@example.com", email.getEmail());
            assertEquals("joao", email.getUserName());
            assertFalse(email.isExpired());
        }

        @Test
        @DisplayName("deve gerar código alfanumérico sem hífens")
        void shouldGenerateCodeWithoutHyphens() {
            final var email = Email.create("user@example.com", "joao");
            assertFalse(email.getCode().contains("-"));
        }

        @Test
        @DisplayName("deve rejeitar email nulo")
        void shouldRejectNullEmail() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Email.create(null, "joao"));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("E-mail")));
        }

        @Test
        @DisplayName("deve rejeitar email em branco")
        void shouldRejectBlankEmail() {
            assertThrows(NotificationException.class, () -> Email.create("   ", "joao"));
        }

    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir Email a partir de dados persistidos")
        void shouldReconstituteFromPersistedData() {
            final var id = EmailID.unique();
            final var now = Instant.now();
            final var expires = now.plus(5, ChronoUnit.MINUTES);

            final var email = Email.with(id, "ABCD1234", "user@test.com", "maria", now, expires);

            assertEquals(id, email.getId());
            assertEquals("ABCD1234", email.getCode());
            assertEquals("user@test.com", email.getEmail());
            assertEquals("maria", email.getUserName());
            assertFalse(email.isExpired());
        }

        @Test
        @DisplayName("não deve validar dados ao reconstituir, mesmo que o código seja nulo")
        void shouldNotValidateOnReconstitution() {
            final var id = EmailID.unique();
            final var now = Instant.now();

            final var email = Email.with(id, null, "user@test.com", "joao", now, now.plus(5, ChronoUnit.MINUTES));

            assertNull(email.getCode());
        }
    }

    @Nested
    @DisplayName("Expiração")
    class Expiration {

        @Test
        @DisplayName("deve detectar email não expirado")
        void shouldDetectNonExpired() {
            final var email = Email.create("user@example.com", "joao");
            assertFalse(email.isExpired());
        }

        @Test
        @DisplayName("deve detectar email expirado")
        void shouldDetectExpired() {
            final var past = Instant.now().minus(10, ChronoUnit.MINUTES);
            final var email = Email.with(
                    EmailID.unique(), "XPTO1234", "user@example.com", "joao",
                    past.minus(5, ChronoUnit.MINUTES), past
            );
            assertTrue(email.isExpired());
        }
    }
}
