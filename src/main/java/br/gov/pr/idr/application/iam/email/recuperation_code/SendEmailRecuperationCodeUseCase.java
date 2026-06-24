package br.gov.pr.idr.application.iam.email.recuperation_code;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.VoidUseCase;
import br.gov.pr.idr.domain.iam.email.Email;
import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import br.gov.pr.idr.domain.iam.email.EmailGateway;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;

@CommandUseCase
public class SendEmailRecuperationCodeUseCase extends VoidUseCase<SendEmailRecuperationCodeCommand> {

    private final UserGateway userGateway;
    private final EmailGateway emailGateway;
    private final SendEmailGateway sendEmailGateway;

    public SendEmailRecuperationCodeUseCase(
            final UserGateway userGateway,
            final EmailGateway emailGateway,
            final SendEmailGateway sendEmailGateway
                                           ) {
        this.userGateway = userGateway;
        this.emailGateway = emailGateway;
        this.sendEmailGateway = sendEmailGateway;
    }

    @Override
    public void execute(final SendEmailRecuperationCodeCommand command) {
        final var user = userGateway.findByUsername(command.email())
                .orElseThrow(() -> new UserException(
                        "Usuário com e-mail %s não encontrado".formatted(command.email())));

        emailGateway.deleteByEmail(user.getUsername());

        final var emailRequest = Email.create(user.getUsername(), user.getName());
        emailGateway.save(emailRequest);

        sendEmailGateway.send(
                emailRequest.getEmail(),
                emailRequest.getUserName(),
                emailRequest.getCode()
        );
    }
}
