package br.gov.pr.idr.infra.iam.user.events;

import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import br.gov.pr.idr.domain.iam.user.events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {

    private final SendEmailGateway emailGateway;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreated(final UserCreatedEvent event) {
        emailGateway.sendWelcome(event.email(), event.name());
    }
}
