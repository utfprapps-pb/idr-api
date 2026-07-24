package br.gov.pr.idr.application.iam.user.retries.search;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SearchUserOutput")
class SearchUserOutputTest {

    private User userWith(final Set<Permission> permissions) {
        final var now = Instant.now();
        return User.with(
                UserID.unique(), "João Silva", "joao.silva",
                Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"),
                "41999999999",
                CityID.unique(),
                "80000-000", "Rua das Flores", "123", "CREA-1234", "2010",
                now, now, true, permissions
        );
    }

    @Test
    @DisplayName("deve mapear a role da primeira permissão quando o usuário possui permissões")
    void shouldMapRoleWhenUserHasPermissions() {
        final var permission = Permission.create(UserRole.ADMIN, false, Set.of(), Set.of());
        final var user = userWith(Set.of(permission));

        final var output = SearchUserOutput.from(user);

        assertEquals(user.getId().id(), output.id());
        assertEquals(user.getName(), output.name());
        assertEquals(user.getUsername(), output.username());
        assertEquals(user.getPhone(), output.phone());
        assertEquals(user.getProfessionalRegister(), output.professionalRegister());
        assertEquals(user.getGraduationYear(), output.graduationYear());
        assertEquals(user.getCityID().id(), output.cityId());
        assertEquals(user.isActive(), output.active());
        assertEquals(user.getCreatedAt(), output.createdAt());
        assertEquals(UserRole.ADMIN, output.role());
    }

    @Test
    @DisplayName("deve retornar role nula quando o usuário não possui permissões")
    void shouldReturnNullRoleWhenUserHasNoPermissions() {
        final var user = userWith(Set.of());

        final var output = SearchUserOutput.from(user);

        assertNull(output.role());
    }
}
