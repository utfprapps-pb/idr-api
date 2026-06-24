package br.gov.pr.idr.application.iam.email.reset_password;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.VoidUseCase;
import br.gov.pr.idr.domain.iam.email.EmailException;
import br.gov.pr.idr.domain.iam.email.EmailGateway;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.events.PasswordResetEvent;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.shared.events.DomainEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

@CommandUseCase
public class ResetPasswordUseCase extends VoidUseCase<ResetPasswordCommand> {

    private final UserGateway userGateway;
    private final EmailGateway emailGateway;
    private final DomainEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordUseCase(
            final UserGateway userGateway,
            final EmailGateway emailGateway,
            final DomainEventPublisher eventPublisher,
            final PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.emailGateway = emailGateway;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(final ResetPasswordCommand command) {
        final var emailRecord = emailGateway.findByEmailAndCode(command.email(), command.code())
                .orElseThrow(() -> new EmailException("Código de recuperação inválido"));

        if (emailRecord.isExpired()) {
            throw new EmailException("Código de recuperação expirado");
        }

        final var user = userGateway.findByUsername(command.email())
                .orElseThrow(() -> new UserException(
                        "Usuário com e-mail %s não encontrado".formatted(command.email())));

        Password.from(command.password(), command.confirmPassword());

        final var encodedPassword = passwordEncoder.encode(command.password());

        userGateway.updatePassword(user, encodedPassword);
        emailGateway.deleteByEmail(command.email());
        eventPublisher.publish(new PasswordResetEvent(user.getUsername(), user.getName()));
    }
}
