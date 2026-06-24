package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.update.ToggleUserActiveUseCase;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ToggleUserActiveUseCase")
class ToggleUserActiveUseCaseTest {

    @Mock UserGateway userGateway;
    @InjectMocks ToggleUserActiveUseCase useCase;

    private User testUser() {
        return User.create("Pedro", "pedro", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), "41966666666", CityID.unique(),
                "80003-000", "Rua A", "1", "CREA-3", "2018", Set.of());
    }

    @Test
    @DisplayName("deve alternar active de false para true e salvar")
    void shouldToggleFromFalseToTrue() {
        final var user = testUser();
        assertFalse(user.isActive());
        when(userGateway.findById(any())).thenReturn(Optional.of(user));
        when(userGateway.update(any())).thenReturn(user);

        useCase.execute(UUID.randomUUID());

        verify(userGateway).update(argThat(User::isActive));
    }

    @Test
    @DisplayName("deve lançar UserException quando usuário não é encontrado")
    void shouldThrowWhenUserNotFound() {
        when(userGateway.findById(any())).thenReturn(Optional.empty());
        var uuid = UUID.randomUUID();
        assertThrows(UserException.class, () -> useCase.execute(uuid));
        verify(userGateway, never()).update(any());
    }
}
