package br.gov.pr.idr.application.iam.user;

import br.gov.pr.idr.application.iam.user.retries.find.FindUserByIdUseCase;
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
@DisplayName("FindUserByIdUseCase")
class FindUserByIdUseCaseTest {

    @Mock UserGateway userGateway;
    @InjectMocks FindUserByIdUseCase useCase;

    private User testUser() {
        return User.create("Maria", "maria", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), "41988888888", CityID.unique(),
                "80001-000", "Av. Brasil", "10", "CREA-1", "2015", Set.of());
    }

    @Test
    @DisplayName("deve retornar output quando usuário é encontrado")
    void shouldReturnOutputWhenFound() {
        final var user = testUser();
        when(userGateway.findById(any())).thenReturn(Optional.of(user));

        final var output = useCase.execute(UUID.randomUUID());

        assertNotNull(output);
        assertEquals("Maria", output.name());
        assertEquals("maria", output.username());
    }

    @Test
    @DisplayName("deve lançar UserException quando usuário não é encontrado")
    void shouldThrowWhenNotFound() {
        when(userGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> useCase.execute(UUID.randomUUID()));
    }

    @Test
    @DisplayName("deve retornar role, regionIds e cityIds quando usuário tem permissão")
    void shouldReturnPermissionFieldsWhenUserHasPermission() {
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var permission = Permission.create(UserRole.TECNICO, true, Set.of(regionId), Set.of(cityId));
        final var user = User.create("Carlos", "carlos", Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"), "41977777777", CityID.unique(),
                "80002-000", "Rua A", "5", "CREA-2", "2018", Set.of(permission));
        when(userGateway.findById(any())).thenReturn(Optional.of(user));

        final var output = useCase.execute(UUID.randomUUID());

        assertEquals(UserRole.TECNICO, output.role());
        assertTrue(output.readOnly());
        assertTrue(output.regionIds().contains(regionId));
        assertTrue(output.cityIds().contains(cityId));
    }
}
