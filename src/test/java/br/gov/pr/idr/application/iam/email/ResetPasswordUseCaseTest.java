package br.gov.pr.idr.application.iam.email;

import br.gov.pr.idr.application.iam.email.reset_password.ResetPasswordCommand;
import br.gov.pr.idr.application.iam.email.reset_password.ResetPasswordUseCase;
import br.gov.pr.idr.domain.iam.email.Email;
import br.gov.pr.idr.domain.iam.email.EmailException;
import br.gov.pr.idr.domain.iam.email.EmailGateway;
import br.gov.pr.idr.domain.iam.email.EmailID;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.events.DomainEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResetPasswordUseCase")
class ResetPasswordUseCaseTest {

    @Mock UserGateway userGateway;
    @Mock EmailGateway emailGateway;
    @Mock DomainEventPublisher eventPublisher;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks ResetPasswordUseCase useCase;

    private static final String EMAIL = "user@test.com";
    private static final String CODE = "ABCD1234";
    private static final String NEW_PWD = "Nova@Senha1";

    private Email validEmailRecord() {
        final var now = Instant.now();
        return Email.with(EmailID.unique(), CODE, EMAIL, "user", now, now.plus(5, ChronoUnit.MINUTES));
    }

    private Email expiredEmailRecord() {
        final var past = Instant.now().minus(10, ChronoUnit.MINUTES);
        return Email.with(EmailID.unique(), CODE, EMAIL, "user", past.minus(5, ChronoUnit.MINUTES), past);
    }

    private User testUser() {
        return User.create("user", EMAIL, Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), null, CityID.unique(), null, null, null, null, null, Set.of());
    }

    private ResetPasswordCommand validCommand() {
        return new ResetPasswordCommand(EMAIL, CODE, NEW_PWD, NEW_PWD);
    }

    @Test
    @DisplayName("deve resetar senha com sucesso e publicar evento")
    void shouldResetPasswordAndPublishEvent() {
        when(emailGateway.findByEmailAndCode(EMAIL, CODE)).thenReturn(Optional.of(validEmailRecord()));
        when(userGateway.findByUsername(EMAIL)).thenReturn(Optional.of(testUser()));
        when(passwordEncoder.encode(NEW_PWD)).thenReturn("encoded");

        useCase.execute(validCommand());

        verify(userGateway).updatePassword(any(), anyString());
        verify(emailGateway).deleteByEmail(EMAIL);
        verify(eventPublisher).publish(any());
    }

    @Test
    @DisplayName("deve lançar EmailException quando código não é encontrado")
    void shouldThrowWhenCodeNotFound() {
        when(emailGateway.findByEmailAndCode(anyString(), anyString())).thenReturn(Optional.empty());
        final var cmd = validCommand();

        assertThrows(EmailException.class, () -> useCase.execute(cmd));
    }

    @Test
    @DisplayName("deve lançar EmailException quando código está expirado")
    void shouldThrowWhenCodeExpired() {
        when(emailGateway.findByEmailAndCode(EMAIL, CODE)).thenReturn(Optional.of(expiredEmailRecord()));
        final var cmd = validCommand();

        assertThrows(EmailException.class, () -> useCase.execute(cmd));
    }

    @Test
    @DisplayName("deve lançar UserException quando usuário não é encontrado pelo email")
    void shouldThrowWhenUserNotFound() {
        when(emailGateway.findByEmailAndCode(EMAIL, CODE)).thenReturn(Optional.of(validEmailRecord()));
        when(userGateway.findByUsername(EMAIL)).thenReturn(Optional.empty());
        final var cmd = validCommand();

        assertThrows(UserException.class, () -> useCase.execute(cmd));
    }
}
