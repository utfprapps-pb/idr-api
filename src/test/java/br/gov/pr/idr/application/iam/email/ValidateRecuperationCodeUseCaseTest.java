package br.gov.pr.idr.application.iam.email;

import br.gov.pr.idr.application.iam.email.validate_code.ValidateRecuperationCodeCommand;
import br.gov.pr.idr.application.iam.email.validate_code.ValidateRecuperationCodeUseCase;
import br.gov.pr.idr.domain.iam.email.Email;
import br.gov.pr.idr.domain.iam.email.EmailException;
import br.gov.pr.idr.domain.iam.email.EmailGateway;
import br.gov.pr.idr.domain.iam.email.EmailID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidateRecuperationCodeUseCase")
class ValidateRecuperationCodeUseCaseTest {

    @Mock EmailGateway emailGateway;
    @InjectMocks ValidateRecuperationCodeUseCase useCase;

    private static final String EMAIL = "user@test.com";
    private static final String CODE = "ABCD1234";

    private Email validEmail() {
        final var now = Instant.now();
        return Email.with(EmailID.unique(), CODE, EMAIL, "user", now, now.plus(5, ChronoUnit.MINUTES));
    }

    private Email expiredEmail() {
        final var past = Instant.now().minus(10, ChronoUnit.MINUTES);
        return Email.with(EmailID.unique(), CODE, EMAIL, "user", past.minus(5, ChronoUnit.MINUTES), past);
    }

    @Test
    @DisplayName("deve aceitar código válido e não expirado")
    void shouldAcceptValidCode() {
        when(emailGateway.findByEmailAndCode(EMAIL, CODE)).thenReturn(Optional.of(validEmail()));
        final var cmd = ValidateRecuperationCodeCommand.from(EMAIL, CODE);

        assertDoesNotThrow(() -> useCase.execute(cmd));
    }

    @Test
    @DisplayName("deve lançar EmailException quando código não é encontrado")
    void shouldThrowWhenCodeNotFound() {
        when(emailGateway.findByEmailAndCode(anyString(), anyString())).thenReturn(Optional.empty());
        final var cmd = ValidateRecuperationCodeCommand.from(EMAIL, "INVALIDO");

        assertThrows(EmailException.class, () -> useCase.execute(cmd));
    }

    @Test
    @DisplayName("deve lançar EmailException quando código está expirado")
    void shouldThrowWhenCodeExpired() {
        when(emailGateway.findByEmailAndCode(EMAIL, CODE)).thenReturn(Optional.of(expiredEmail()));
        final var cmd = ValidateRecuperationCodeCommand.from(EMAIL, CODE);

        assertThrows(EmailException.class, () -> useCase.execute(cmd));
    }
}
