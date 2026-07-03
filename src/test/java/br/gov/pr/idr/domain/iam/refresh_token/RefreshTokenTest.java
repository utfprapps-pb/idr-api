package br.gov.pr.idr.domain.iam.refresh_token;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RefreshToken — Entity")
class RefreshTokenTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "joao.silva";

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar token com dados válidos e não revogado")
        void shouldCreateValidToken() {
            final var token = RefreshToken.create(USER_ID, USERNAME, 7L);

            assertNotNull(token.getId());
            assertNotNull(token.getToken());
            assertEquals(USER_ID, token.getUserId());
            assertEquals(USERNAME, token.getUsername());
            assertFalse(token.isRevoked());
            assertFalse(token.isExpired());
        }

        @Test
        @DisplayName("deve calcular expiração corretamente com base nos dias")
        void shouldCalculateExpirationFromDays() {
            final var before = Instant.now();
            final var token = RefreshToken.create(USER_ID, USERNAME, 7L);
            final var after = Instant.now();

            final var expectedMin = before.plusSeconds(7 * 24 * 60 * 60);
            final var expectedMax = after.plusSeconds(7 * 24 * 60 * 60);

            assertFalse(token.getExpiresAt().isBefore(expectedMin));
            assertFalse(token.getExpiresAt().isAfter(expectedMax));
        }

        @Test
        @DisplayName("deve rejeitar token sem userId")
        void shouldRejectNullUserId() {
            final var ex = assertThrows(NotificationException.class,
                    () -> RefreshToken.create(null, USERNAME, 7L));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("UserId")));
        }

        @Test
        @DisplayName("deve rejeitar token sem username")
        void shouldRejectNullUsername() {
            final var ex = assertThrows(NotificationException.class,
                    () -> RefreshToken.create(USER_ID, null, 7L));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Username")));
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir token a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = RefreshTokenID.unique();
            final var tokenStr = UUID.randomUUID().toString();
            final var now = Instant.now();
            final var expires = now.plus(7, ChronoUnit.DAYS);

            final var token = RefreshToken.with(id, tokenStr, USER_ID, USERNAME, expires, now, false);

            assertEquals(id, token.getId());
            assertEquals(tokenStr, token.getToken());
            assertFalse(token.isRevoked());
        }

        @Test
        @DisplayName("não deve validar dados ao reconstituir, mesmo que o token seja nulo")
        void shouldNotValidateOnReconstitution() {
            final var id = RefreshTokenID.unique();
            final var now = Instant.now();
            final var expires = now.plus(7, ChronoUnit.DAYS);

            final var token = RefreshToken.with(id, null, USER_ID, USERNAME, expires, now, false);

            assertNull(token.getToken());
        }
    }

    @Nested
    @DisplayName("Revogação")
    class Revocation {

        @Test
        @DisplayName("deve revogar token")
        void shouldRevokeToken() {
            final var token = RefreshToken.create(USER_ID, USERNAME, 7L);
            assertFalse(token.isRevoked());

            token.revoke();

            assertTrue(token.isRevoked());
        }
    }

    @Nested
    @DisplayName("Expiração")
    class Expiration {

        @Test
        @DisplayName("deve detectar token não expirado")
        void shouldDetectNonExpired() {
            final var token = RefreshToken.create(USER_ID, USERNAME, 7L);
            assertFalse(token.isExpired());
        }

        @Test
        @DisplayName("deve detectar token expirado")
        void shouldDetectExpired() {
            final var past = Instant.now().minus(1, ChronoUnit.DAYS);
            final var token = RefreshToken.with(
                    RefreshTokenID.unique(),
                    UUID.randomUUID().toString(),
                    USER_ID, USERNAME, past, past.minus(7, ChronoUnit.DAYS), false
            );
            assertTrue(token.isExpired());
        }
    }
}
