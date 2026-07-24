package br.gov.pr.idr.infra.iam.user.persistence;

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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserJPAEntity")
class UserJPAEntityTest {

    private static final String ENCODED_PASSWORD = "Encoded@123";

    private User userWith(final boolean active, final Set<Permission> permissions) {
        final var now = Instant.now();
        return User.with(
                UserID.unique(), "João Silva", "joao.silva",
                Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"),
                "41999999999",
                CityID.unique(),
                "80000-000", "Rua das Flores", "123", "CREA-1234", "2010",
                now, now, active, permissions
        );
    }

    private Permission adminPermission() {
        return Permission.create(UserRole.ADMIN, false,
                Set.of(UUID.randomUUID()), Set.of(UUID.randomUUID()));
    }

    @Test
    @DisplayName("deve retornar as permissões do usuário como authorities")
    void shouldReturnAuthoritiesFromPermissions() {
        final var permission = adminPermission();
        final var user = userWith(true, Set.of(permission));
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        final var authorities = entity.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("deve retornar lista de authorities vazia quando não há permissões")
    void shouldReturnEmptyAuthoritiesWhenNoPermissions() {
        final var user = userWith(true, Set.of());
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        final var authorities = entity.getAuthorities();

        assertTrue(authorities.isEmpty());
    }

    @Test
    @DisplayName("deve retornar a senha codificada")
    void shouldReturnPassword() {
        final var user = userWith(true, Set.of());
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        assertEquals(ENCODED_PASSWORD, entity.getPassword());
    }

    @Test
    @DisplayName("deve estar habilitado quando o usuário está ativo")
    void shouldBeEnabledWhenActive() {
        final var user = userWith(true, Set.of());
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        assertTrue(entity.isEnabled());
    }

    @Test
    @DisplayName("deve estar desabilitado quando o usuário está inativo")
    void shouldBeDisabledWhenInactive() {
        final var user = userWith(false, Set.of());
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        assertFalse(entity.isEnabled());
    }

    @Test
    @DisplayName("deve converter para o domínio incluindo as permissões")
    void shouldMapToDomainWithPermissions() {
        final var permission = adminPermission();
        final var user = userWith(true, Set.of(permission));
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        final var domain = entity.toDomain();

        assertEquals(user.getId(), domain.getId());
        assertEquals(user.getName(), domain.getName());
        assertEquals(user.getUsername(), domain.getUsername());
        assertEquals(user.getCpf(), domain.getCpf());
        assertEquals(user.getPhone(), domain.getPhone());
        assertEquals(user.getCityID(), domain.getCityID());
        assertEquals(user.getCep(), domain.getCep());
        assertEquals(user.getStreet(), domain.getStreet());
        assertEquals(user.getHouseNumber(), domain.getHouseNumber());
        assertEquals(user.getProfessionalRegister(), domain.getProfessionalRegister());
        assertEquals(user.getGraduationYear(), domain.getGraduationYear());
        assertEquals(user.getCreatedAt(), domain.getCreatedAt());
        assertEquals(user.getUpdatedAt(), domain.getUpdatedAt());
        assertEquals(user.isActive(), domain.isActive());
        assertEquals(ENCODED_PASSWORD, domain.getPassword().pasword());

        assertEquals(1, domain.getPermissions().size());
        final var mappedPermission = domain.getPermissions().iterator().next();
        assertEquals(permission.getId(), mappedPermission.getId());
        assertEquals(permission.getRole(), mappedPermission.getRole());
        assertEquals(permission.isReadOnly(), mappedPermission.isReadOnly());
        assertEquals(permission.getRegionIds(), mappedPermission.getRegionIds());
        assertEquals(permission.getCityIds(), mappedPermission.getCityIds());
    }

    @Test
    @DisplayName("deve converter para o domínio sem permissões")
    void shouldMapToDomainWithoutPermissions() {
        final var user = userWith(false, Set.of());
        final var entity = UserJPAEntity.from(user, ENCODED_PASSWORD);

        final var domain = entity.toDomain();

        assertTrue(domain.getPermissions().isEmpty());
        assertFalse(domain.isActive());
    }
}
