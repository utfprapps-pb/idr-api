package br.gov.pr.idr.application.property_management.property;

import br.gov.pr.idr.application.property_management.property.retrieve.search.PropertyAccessPolicy;
import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PropertyAccessPolicy")
class PropertyAccessPolicyTest {

    private User userWith(final UserRole role, final Set<UUID> regionIds, final Set<UUID> cityIds) {
        final var permission = Permission.create(role, false, regionIds, cityIds);
        return User.create(
                "Usuário", "user", Password.from("Senha@123", "Senha@123"), CPF.from("529.982.247-25"),
                "99999999", CityID.unique(), "80000000", "Rua", "10", "REG-1", "2020", Set.of(permission));
    }

    private User userWithoutPermissions() {
        return User.create(
                "Usuário", "user", Password.from("Senha@123", "Senha@123"), CPF.from("529.982.247-25"),
                "99999999", CityID.unique(), "80000000", "Rua", "10", "REG-1", "2020", Set.of());
    }

    @Test
    @DisplayName("deve resolver escopo irrestrito para ADMIN")
    void shouldResolveUnrestrictedScopeForAdmin() {
        final var user = userWith(UserRole.ADMIN, Set.of(), Set.of());

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.UNRESTRICTED, scope.type());
    }

    @Test
    @DisplayName("deve resolver escopo irrestrito para COORDENACAO_GERAL")
    void shouldResolveUnrestrictedScopeForCoordenacaoGeral() {
        final var user = userWith(UserRole.COORDENACAO_GERAL, Set.of(), Set.of());

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.UNRESTRICTED, scope.type());
    }

    @Test
    @DisplayName("deve resolver escopo por localização para GERENCIA_MACRO")
    void shouldResolveLocationScopeForGerenciaMacro() {
        final var regionId = UUID.randomUUID();
        final var user = userWith(UserRole.GERENCIA_MACRO, Set.of(regionId), Set.of());

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.BY_LOCATION, scope.type());
        assertEquals(Set.of(RegionID.from(regionId)), scope.regionIds());
        assertTrue(scope.cityIds().isEmpty());
    }

    @Test
    @DisplayName("deve resolver escopo por localização para GERENCIA_REGIONAL")
    void shouldResolveLocationScopeForGerenciaRegional() {
        final var regionId = UUID.randomUUID();
        final var user = userWith(UserRole.GERENCIA_REGIONAL, Set.of(regionId), Set.of());

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.BY_LOCATION, scope.type());
        assertEquals(Set.of(RegionID.from(regionId)), scope.regionIds());
    }

    @Test
    @DisplayName("deve resolver escopo por localização para GERENCIA_MUNICIPAL")
    void shouldResolveLocationScopeForGerenciaMunicipal() {
        final var cityId = UUID.randomUUID();
        final var user = userWith(UserRole.GERENCIA_MUNICIPAL, Set.of(), Set.of(cityId));

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.BY_LOCATION, scope.type());
        assertEquals(Set.of(CityID.from(cityId)), scope.cityIds());
    }

    @Test
    @DisplayName("deve resolver escopo por técnico para TECNICO")
    void shouldResolveTechnicianScopeForTecnico() {
        final var user = userWith(UserRole.TECNICO, Set.of(), Set.of());

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.BY_TECHNICIAN, scope.type());
        assertEquals(user.getId(), scope.technicianId());
    }

    @Test
    @DisplayName("deve resolver escopo por técnico quando usuário não possui nenhuma permissão")
    void shouldResolveTechnicianScopeWhenUserHasNoPermissions() {
        final var user = userWithoutPermissions();

        final var scope = PropertyAccessPolicy.resolve(user);

        assertEquals(PropertySearchScope.Type.BY_TECHNICIAN, scope.type());
        assertEquals(user.getId(), scope.technicianId());
    }
}
