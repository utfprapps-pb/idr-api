package br.gov.pr.idr.infra.iam.user.events;

import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import br.gov.pr.idr.domain.iam.user.events.PasswordResetEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PasswordResetEventListener {

    private final SendEmailGateway emailGateway;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPasswordReset(final PasswordResetEvent event) {
        emailGateway.sendPasswordResetConfirmation(event.email(), event.name());
    }
}
