package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindUserByUsernameUseCase")
class FindUserByUsernameUseCaseTest {

    @Mock UserGateway userGateway;
    @InjectMocks FindUserByUsernameUseCase useCase;

    private User testUser() {
        return User.create("Ana", "ana.souza", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), "41977777777", CityID.unique(),
                "80002-000", "Rua Nova", "5", "CREA-2", "2016", Set.of());
    }

    @Test
    @DisplayName("deve retornar output quando usuário é encontrado por username")
    void shouldReturnOutputWhenFound() {
        when(userGateway.findByUsername("ana.souza")).thenReturn(Optional.of(testUser()));

        final var output = useCase.execute("ana.souza");

        assertNotNull(output);
        assertEquals("Ana", output.name());
    }

    @Test
    @DisplayName("deve lançar UserException quando username não existe")
    void shouldThrowWhenNotFound() {
        when(userGateway.findByUsername("nao.existe")).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> useCase.execute("nao.existe"));
    }
}
