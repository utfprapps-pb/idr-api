package br.gov.pr.idr.infra.iam.user.events;

import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import br.gov.pr.idr.domain.iam.user.events.PasswordResetEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordResetEventListener")
class PasswordResetEventListenerTest {

    @Mock SendEmailGateway emailGateway;
    @InjectMocks PasswordResetEventListener listener;

    @Test
    @DisplayName("deve enviar confirmação de reset ao receber evento")
    void shouldSendPasswordResetConfirmation() {
        final var event = new PasswordResetEvent("user@test.com", "João");

        listener.onPasswordReset(event);

        verify(emailGateway).sendPasswordResetConfirmation("user@test.com", "João");
    }
}
