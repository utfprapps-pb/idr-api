package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.update.UpdateUserPermissionsCommand;
import br.gov.pr.idr.application.iam.user.update.UpdateUserPermissionsUseCase;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.iam.user.UserRole;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserPermissionsUseCase")
class UpdateUserPermissionsUseCaseTest {

    @Mock UserGateway userGateway;
    @InjectMocks UpdateUserPermissionsUseCase useCase;

    private User testUser() {
        return User.create("Luciana", "luciana", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), null, CityID.unique(), null, null, null, null, null, Set.of());
    }

    @Test
    @DisplayName("deve atualizar permissões do usuário")
    void shouldUpdatePermissions() {
        when(userGateway.findById(any())).thenReturn(Optional.of(testUser()));
        when(userGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var command = UpdateUserPermissionsCommand.from(
                UUID.randomUUID(), UserRole.TECNICO, true, Set.of(), Set.of());

        useCase.execute(command);

        verify(userGateway).update(argThat(u -> !u.getPermissions().isEmpty()));
    }

    @Test
    @DisplayName("deve lançar UserException quando usuário não é encontrado")
    void shouldThrowWhenUserNotFound() {
        when(userGateway.findById(any())).thenReturn(Optional.empty());

        final var command = UpdateUserPermissionsCommand.from(
                UUID.randomUUID(), UserRole.ADMIN, false, Set.of(), Set.of());

        assertThrows(UserException.class, () -> useCase.execute(command));
        verify(userGateway, never()).update(any());
    }
}
