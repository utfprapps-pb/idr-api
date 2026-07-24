package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.create.CreateUserCommand;
import br.gov.pr.idr.application.iam.user.create.CreateUserUseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityGateway;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.tactical.events.DomainEventPublisher;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase")
class CreateUserUseCaseTest {

    @Mock UserGateway userGateway;
    @Mock CityGateway cityGateway;
    @Mock DomainEventPublisher eventPublisher;
    @InjectMocks CreateUserUseCase useCase;

    private static final String NAME = "João Silva";
    private static final String USERNAME = "joao.silva";
    private static final String PASSWORD = "Senha@123";
    private static final String CPF_VALUE = "529.982.247-25";
    private static final UUID CITY_ID = UUID.randomUUID();

    private CreateUserCommand validCommand() {
        return CreateUserCommand.from(NAME, USERNAME, PASSWORD, PASSWORD, CPF_VALUE,
                "41999999999", "2010", "CREA-1", "80000-000", "Rua X", CITY_ID, "100");
    }

    private User savedUser() {
        return User.create(NAME, USERNAME, Password.from(PASSWORD, PASSWORD),
                CPF.from(CPF_VALUE), "41999999999", CityID.from(CITY_ID),
                "80000-000", "Rua X", "100", "CREA-1", "2010", Set.of());
    }

    @Nested
    @DisplayName("Sucesso")
    class Success {

        @Test
        @DisplayName("deve criar usuário e publicar evento")
        void shouldCreateUserAndPublishEvent() {
            final var user = savedUser();
            when(userGateway.existsByUsername(USERNAME)).thenReturn(false);
            when(userGateway.existsByCPF(any())).thenReturn(false);
            when(cityGateway.existsById(any())).thenReturn(true);
            when(userGateway.create(any())).thenReturn(user);

            final var output = useCase.execute(validCommand());

            assertNotNull(output);
            assertEquals(NAME, output.name());
            verify(eventPublisher).publish(any());
            verify(userGateway).create(any());
        }
    }

    @Nested
    @DisplayName("Falhas de negócio")
    class BusinessFailures {

        @Test
        @DisplayName("deve lançar exceção se username já existe")
        void shouldThrowWhenUsernameAlreadyExists() {
            when(userGateway.existsByUsername(USERNAME)).thenReturn(true);
            var command = validCommand();
            assertThrows(NotificationException.class, () -> useCase.execute(command));
            verify(userGateway, never()).create(any());
        }

        @Test
        @DisplayName("deve lançar exceção se CPF já existe")
        void shouldThrowWhenCpfAlreadyExists() {
            when(userGateway.existsByUsername(USERNAME)).thenReturn(false);
            when(userGateway.existsByCPF(any())).thenReturn(true);
            var command = validCommand();
            assertThrows(NotificationException.class, () -> useCase.execute(command));
            verify(userGateway, never()).create(any());
        }

        @Test
        @DisplayName("deve lançar exceção se cidade não existe")
        void shouldThrowWhenCityNotFound() {
            when(userGateway.existsByUsername(USERNAME)).thenReturn(false);
            when(userGateway.existsByCPF(any())).thenReturn(false);
            when(cityGateway.existsById(any())).thenReturn(false);
            var command = validCommand();
            assertThrows(NotificationException.class, () -> useCase.execute(command));
            verify(userGateway, never()).create(any());
        }
    }
}
