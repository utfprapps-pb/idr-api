package br.gov.pr.idr.infra.iam.user.events;

import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import br.gov.pr.idr.domain.iam.user.events.UserCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserCreatedEventListener")
class UserCreatedEventListenerTest {

    @Mock SendEmailGateway emailGateway;
    @InjectMocks UserCreatedEventListener listener;

    @Test
    @DisplayName("deve enviar e-mail de boas-vindas ao receber evento")
    void shouldSendWelcomeEmail() {
        final var event = new UserCreatedEvent("maria@test.com", "Maria");

        listener.onUserCreated(event);

        verify(emailGateway).sendWelcome("maria@test.com", "Maria");
    }
}
