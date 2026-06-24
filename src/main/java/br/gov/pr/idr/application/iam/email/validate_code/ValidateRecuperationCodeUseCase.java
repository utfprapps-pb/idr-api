package br.gov.pr.idr.application.iam.email.validate_code;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.VoidUseCase;
import br.gov.pr.idr.domain.iam.email.EmailException;
import br.gov.pr.idr.domain.iam.email.EmailGateway;

@CommandUseCase
public class ValidateRecuperationCodeUseCase extends VoidUseCase<ValidateRecuperationCodeCommand> {

    private final EmailGateway emailGateway;

    public ValidateRecuperationCodeUseCase(final EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @Override
    public void execute(final ValidateRecuperationCodeCommand command) {
        final var emailRecord = emailGateway.findByEmailAndCode(command.email(), command.code())
                .orElseThrow(() -> new EmailException("Código de recuperação inválido"));

        if (emailRecord.isExpired()) {
            throw new EmailException("Código de recuperação expirado");
        }
    }
}
