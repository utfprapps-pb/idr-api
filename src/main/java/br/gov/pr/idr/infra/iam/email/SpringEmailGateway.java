package br.gov.pr.idr.infra.iam.email;

import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringEmailGateway implements SendEmailGateway {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(
            final String toEmail,
            final String userName,
            final String code
    ) {
        final var ctx = new Context();
        ctx.setVariable("userName", userName);
        ctx.setVariable("recoveryCode", code);

        final String htmlBody = templateEngine.process("email/password-recovery", ctx);

        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("Recuperação de Senha - IDR");
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail", e);
        }
    }

    @Override
    public void sendPasswordResetConfirmation(final String toEmail, final String userName) {
        final var ctx = new Context();
        ctx.setVariable("userName", userName);

        final String htmlBody = templateEngine.process("email/password-reset-confirmation", ctx);

        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("Senha Redefinida com Sucesso - IDR");
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de confirmação para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail de confirmação", e);
        }
    }
}
