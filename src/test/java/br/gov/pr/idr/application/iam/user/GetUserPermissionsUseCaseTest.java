package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.retries.permissions.GetUserPermissionsUseCase;
import br.gov.pr.idr.domain.iam.permission.Permission;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserPermissionsUseCase")
class GetUserPermissionsUseCaseTest {

    @Mock UserGateway userGateway;
    @InjectMocks GetUserPermissionsUseCase useCase;

    private User testUser(final Set<Permission> permissions) {
        return User.create("Maria", "maria", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), "41988888888", CityID.unique(),
                "80001-000", "Av. Brasil", "10", "CREA-1", "2015", permissions);
    }

    @Test
    @DisplayName("deve retornar userId e lista vazia quando usuário não possui permissões")
    void shouldReturnOutputWithNoPermissions() {
        final var user = testUser(Set.of());
        when(userGateway.findById(any())).thenReturn(Optional.of(user));

        final var output = useCase.execute(UUID.randomUUID());

        assertEquals(user.getId().id(), output.userId());
        assertTrue(output.permissions().isEmpty());
    }

    @Test
    @DisplayName("deve mapear cada permissão para role, readOnly, regionIds e cityIds")
    void shouldMapPermissionsToItems() {
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var permission = Permission.create(UserRole.TECNICO, true, Set.of(regionId), Set.of(cityId));
        final var user = testUser(Set.of(permission));
        when(userGateway.findById(any())).thenReturn(Optional.of(user));

        final var output = useCase.execute(UUID.randomUUID());

        assertEquals(1, output.permissions().size());
        final var item = output.permissions().getFirst();
        assertEquals(permission.getId().id(), item.id());
        assertEquals(UserRole.TECNICO, item.role());
        assertTrue(item.readOnly());
        assertEquals(Set.of(regionId), item.regionIds());
        assertEquals(Set.of(cityId), item.cityIds());
    }

    @Test
    @DisplayName("deve lançar UserException quando usuário não é encontrado")
    void shouldThrowWhenNotFound() {
        final var id = UUID.randomUUID();
        when(userGateway.findById(any())).thenReturn(Optional.empty());

        final var exception = assertThrows(UserException.class, () -> useCase.execute(id));
        assertTrue(exception.getMessage().contains(id.toString()));
    }
}
