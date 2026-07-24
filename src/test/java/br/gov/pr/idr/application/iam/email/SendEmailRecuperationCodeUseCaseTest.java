package br.gov.pr.idr.application.iam.email;

import br.gov.pr.idr.application.iam.email.recuperation_code.SendEmailRecuperationCodeCommand;
import br.gov.pr.idr.application.iam.email.recuperation_code.SendEmailRecuperationCodeUseCase;
import br.gov.pr.idr.domain.iam.email.EmailGateway;
import br.gov.pr.idr.domain.iam.email.send.SendEmailGateway;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.exceptions.UserException;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendEmailRecuperationCodeUseCase")
class SendEmailRecuperationCodeUseCaseTest {

    @Mock UserGateway userGateway;
    @Mock EmailGateway emailGateway;
    @Mock SendEmailGateway sendEmailGateway;
    @InjectMocks SendEmailRecuperationCodeUseCase useCase;

    private User testUser() {
        return User.create("Fernando", "fernando@test.com",
                Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), null, CityID.unique(), null, null, null, null, null, Set.of());
    }

    @Test
    @DisplayName("deve enviar código de recuperação por email")
    void shouldSendRecuperationCode() {
        when(userGateway.findByUsername("fernando@test.com")).thenReturn(Optional.of(testUser()));

        useCase.execute(SendEmailRecuperationCodeCommand.from("fernando@test.com"));

        verify(emailGateway).deleteByEmail(anyString());
        verify(emailGateway).save(any());
        verify(sendEmailGateway).send(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("deve lançar UserException quando email não é encontrado")
    void shouldThrowWhenEmailNotFound() {
        when(userGateway.findByUsername("nao@existe.com")).thenReturn(Optional.empty());
        final var cmd = SendEmailRecuperationCodeCommand.from("nao@existe.com");

        assertThrows(UserException.class, () -> useCase.execute(cmd));
        verify(sendEmailGateway, never()).send(anyString(), anyString(), anyString());
    }
}
