package br.gov.pr.idr.domain.iam.user.vo;

import br.gov.pr.idr.domain.iam.user.exceptions.PasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Password — Value Object")
class PasswordTest {

    private static final String VALID = "Senha@123";

    @Nested
    @DisplayName("Criação válida")
    class Valid {

        @Test
        @DisplayName("deve criar Password com senha forte e confirmação idêntica")
        void shouldCreateWithStrongPassword() {
            final var pwd = Password.from(VALID, VALID);
            assertNotNull(pwd);
            assertEquals(VALID, pwd.pasword());
            assertEquals(VALID, pwd.confirmPassword());
        }
    }

    @Nested
    @DisplayName("Criação inválida")
    class Invalid {

        @Test
        @DisplayName("deve rejeitar senha nula")
        void shouldRejectNullPassword() {
            final var ex = assertThrows(PasswordException.class, () -> Password.from(null, VALID));
            assertEquals("password", ex.getErrors().getFirst().field());
        }

        @Test
        @DisplayName("deve rejeitar confirmação nula")
        void shouldRejectNullConfirmation() {
            final var ex = assertThrows(PasswordException.class, () -> Password.from(VALID, null));
            assertEquals("confirmPassword", ex.getErrors().getFirst().field());
        }

        @Test
        @DisplayName("deve rejeitar senhas que não conferem")
        void shouldRejectMismatch() {
            final var ex = assertThrows(PasswordException.class, () -> Password.from(VALID, "Outra@123"));
            assertEquals("password", ex.getErrors().getFirst().field());
            assertTrue(ex.getMessage().contains("conferem"));
        }

        @Test
        @DisplayName("deve rejeitar senha com menos de 8 caracteres")
        void shouldRejectShortPassword() {
            assertThrows(PasswordException.class, () -> Password.from("Ab@1", "Ab@1"));
        }

        @Test
        @DisplayName("deve rejeitar senha sem letra maiúscula")
        void shouldRejectNoUpperCase() {
            assertThrows(PasswordException.class, () -> Password.from("senha@123", "senha@123"));
        }

        @Test
        @DisplayName("deve rejeitar senha sem letra minúscula")
        void shouldRejectNoLowerCase() {
            assertThrows(PasswordException.class, () -> Password.from("SENHA@123", "SENHA@123"));
        }

        @Test
        @DisplayName("deve rejeitar senha sem número")
        void shouldRejectNoDigit() {
            assertThrows(PasswordException.class, () -> Password.from("Senha@abc", "Senha@abc"));
        }

        @Test
        @DisplayName("deve rejeitar senha sem caractere especial")
        void shouldRejectNoSpecialChar() {
            assertThrows(PasswordException.class, () -> Password.from("Senha1234", "Senha1234"));
        }
    }
}
