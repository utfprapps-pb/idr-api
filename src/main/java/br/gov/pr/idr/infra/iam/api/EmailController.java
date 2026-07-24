package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.email.recuperation_code.SendEmailRecuperationCodeCommand;
import br.gov.pr.idr.application.iam.email.recuperation_code.SendEmailRecuperationCodeUseCase;
import br.gov.pr.idr.application.iam.email.reset_password.ResetPasswordCommand;
import br.gov.pr.idr.application.iam.email.reset_password.ResetPasswordUseCase;
import br.gov.pr.idr.application.iam.email.validate_code.ValidateRecuperationCodeCommand;
import br.gov.pr.idr.application.iam.email.validate_code.ValidateRecuperationCodeUseCase;
import br.gov.pr.idr.infra.iam.email.models.recuperation_code.EmailRecuperationCodeRequest;
import br.gov.pr.idr.infra.iam.email.models.recuperation_code.EmailRecuperationCodeResponse;
import br.gov.pr.idr.infra.iam.email.models.recuperation_code.validate.ValidateRecuperationCodeRequest;
import br.gov.pr.idr.infra.iam.email.models.recuperation_code.validate.ValidateRecuperationCodeResponse;
import br.gov.pr.idr.infra.iam.email.models.reset_password.ResetPasswordRequest;
import br.gov.pr.idr.infra.iam.email.models.reset_password.ResetPasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email")
public class EmailController {

    private final SendEmailRecuperationCodeUseCase sendEmailRecuperationCodeUseCase;
    private final ValidateRecuperationCodeUseCase validateRecuperationCodeUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @PostMapping("/send-recuperation-code")
    public ResponseEntity<EmailRecuperationCodeResponse> send(@RequestBody EmailRecuperationCodeRequest request) {
        sendEmailRecuperationCodeUseCase.execute(SendEmailRecuperationCodeCommand.from(request.email()));
        return ResponseEntity.ok(EmailRecuperationCodeResponse.ok());
    }

    @PostMapping("/validate-recuperation-code")
    public ResponseEntity<ValidateRecuperationCodeResponse> validate(@RequestBody ValidateRecuperationCodeRequest request) {
        validateRecuperationCodeUseCase.execute(ValidateRecuperationCodeCommand.from(request.email(), request.code()));
        return ResponseEntity.ok(ValidateRecuperationCodeResponse.ok());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        ResetPasswordCommand command = ResetPasswordCommand.from(
                request.email(),
                request.code(),
                request.password(),
                request.confirmPassword());
        resetPasswordUseCase.execute(command);
        return ResponseEntity.ok(ResetPasswordResponse.ok());
    }
}
