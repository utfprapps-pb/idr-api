package br.gov.pr.idr.infra.iam.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringEmailGateway")
class SpringEmailGatewayTest {

    @Mock JavaMailSender mailSender;
    @Mock TemplateEngine templateEngine;
    @Mock MimeMessage mimeMessage;
    @InjectMocks SpringEmailGateway gateway;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(gateway, "from", "noreply@idr.pr.gov.br");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(any(String.class), any(IContext.class))).thenReturn("<html>body</html>");
    }

    @Test
    @DisplayName("deve processar template e enviar e-mail de recuperação de senha")
    void shouldSendRecuperationEmail() {
        gateway.send("user@test.com", "João", "ABCD1234");

        verify(templateEngine).process(eq("email/password-recovery"), any(IContext.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("deve processar template e enviar e-mail de boas-vindas")
    void shouldSendWelcomeEmail() {
        gateway.sendWelcome("user@test.com", "Maria");

        verify(templateEngine).process(eq("email/welcome"), any(IContext.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("deve processar template e enviar e-mail de confirmação de reset")
    void shouldSendPasswordResetConfirmation() {
        gateway.sendPasswordResetConfirmation("user@test.com", "Carlos");

        verify(templateEngine).process(eq("email/password-reset-confirmation"), any(IContext.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("deve lançar RuntimeException quando envio de recuperação falha")
    void shouldThrowWhenSendFails() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> gateway.send("user@test.com", "João", "ABCD1234"));
    }

    @Test
    @DisplayName("deve lançar RuntimeException quando envio de boas-vindas falha")
    void shouldThrowWhenSendWelcomeFails() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> gateway.sendWelcome("user@test.com", "Maria"));
    }

    @Test
    @DisplayName("deve lançar RuntimeException quando envio de confirmação falha")
    void shouldThrowWhenSendConfirmationFails() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> gateway.sendPasswordResetConfirmation("user@test.com", "Carlos"));
    }
}
