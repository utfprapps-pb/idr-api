package br.gov.pr.idr.domain.iam.permission;

import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Permission — Entity")
class PermissionTest {

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar permissão com role e readOnly")
        void shouldCreatePermission() {
            final var perm = Permission.create(UserRole.TECNICO, true, Set.of(), Set.of());

            assertNotNull(perm.getId());
            assertEquals(UserRole.TECNICO, perm.getRole());
            assertTrue(perm.isReadOnly());
        }

        @Test
        @DisplayName("deve criar permissão com regionIds e cityIds")
        void shouldCreateWithRegionsAndCities() {
            final var regionId = UUID.randomUUID();
            final var cityId = UUID.randomUUID();

            final var perm = Permission.create(UserRole.GERENCIA_REGIONAL, false,
                    Set.of(regionId), Set.of(cityId));

            assertTrue(perm.getRegionIds().contains(regionId));
            assertTrue(perm.getCityIds().contains(cityId));
        }

        @Test
        @DisplayName("deve criar permissão com sets nulos convertendo para vazio")
        void shouldHandleNullSets() {
            final var perm = Permission.create(UserRole.ADMIN, false, null, null);

            assertNotNull(perm.getRegionIds());
            assertNotNull(perm.getCityIds());
            assertTrue(perm.getRegionIds().isEmpty());
            assertTrue(perm.getCityIds().isEmpty());
        }

        @Test
        @DisplayName("deve rejeitar permissão sem role")
        void shouldRejectNullRole() {
            final Set<UUID> empty = Set.of();
            final var ex = assertThrows(NotificationException.class,
                    () -> Permission.create(null, false, empty, empty));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Role")));
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir permissão a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = PermissionID.unique();
            final var perm = Permission.with(id, UserRole.COORDENACAO_GERAL, true, Set.of(), Set.of());

            assertEquals(id, perm.getId());
            assertEquals(UserRole.COORDENACAO_GERAL, perm.getRole());
        }
    }

    @Nested
    @DisplayName("Todos os roles")
    class AllRoles {

        @Test
        @DisplayName("deve aceitar todos os UserRole válidos")
        void shouldAcceptAllRoles() {
            final Set<UUID> empty = Set.of();
            for (final var role : UserRole.values()) {
                assertDoesNotThrow(() -> Permission.create(role, false, empty, empty),
                        "Deveria aceitar role: " + role);
            }
        }
    }
}
