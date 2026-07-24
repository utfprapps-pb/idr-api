package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User — Aggregate")
class UserTest {

    private static final String VALID_CPF = "529.982.247-25";
    private static final String VALID_PWD = "Senha@123";
    private static final Set<Permission> NO_PERMISSIONS = Set.of();
    private CPF cpf;
    private Password password;
    private CityID cityID;

    @BeforeEach
    void setUp() {
        cpf = CPF.from(VALID_CPF);
        password = Password.from(VALID_PWD, VALID_PWD);
        cityID = CityID.unique();
    }

    private User validUser() {
        return User.create(
                "João Silva",
                "joao.silva",
                password,
                cpf,
                "41999999999",
                cityID,
                "80000-000",
                "Rua das Flores",
                "123",
                "CREA-12345",
                "2010",
                Set.of()
        );
    }

    @Nested
    @DisplayName("Criação")
    class Create {

        @Test
        @DisplayName("deve criar usuário válido com active=false por padrão")
        void shouldCreateValidUserAsInactive() {
            final var user = validUser();

            assertNotNull(user.getId());
            assertEquals("João Silva", user.getName());
            assertEquals("joao.silva", user.getUsername());
            assertFalse(user.isActive());
            assertNotNull(user.getCreatedAt());
            assertNotNull(user.getUpdatedAt());
        }

        @Test
        @DisplayName("deve rejeitar usuário sem nome")
        void shouldRejectNullName() {
            final var ex = assertThrows(NotificationException.class, () ->
                    User.create(null, "joao", password, cpf, null, null, null, null, null, null, null, NO_PERMISSIONS)
            );
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome")));
        }

        @Test
        @DisplayName("deve rejeitar usuário com nome em branco")
        void shouldRejectBlankName() {
            assertThrows(NotificationException.class, () ->
                    User.create("  ", "joao", password, cpf, null, null, null, null, null, null, null, NO_PERMISSIONS)
            );
        }

        @Test
        @DisplayName("deve rejeitar usuário sem username")
        void shouldRejectNullUsername() {
            final var ex = assertThrows(NotificationException.class, () ->
                    User.create("João", null, password, cpf, null, null, null, null, null, null, null, NO_PERMISSIONS)
            );
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("UserName")));
        }

        @Test
        @DisplayName("deve rejeitar usuário sem CPF")
        void shouldRejectNullCpf() {
            final var ex = assertThrows(NotificationException.class, () ->
                    User.create("João", "joao", password, null, null, null, null, null, null, null, null, NO_PERMISSIONS)
            );
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("CPF")));
        }

        @Test
        @DisplayName("deve acumular múltiplos erros de validação")
        void shouldAccumulateMultipleErrors() {
            final var ex = assertThrows(NotificationException.class, () ->
                    User.create(null, null, password, null, null, null, null, null, null, null, null, NO_PERMISSIONS)
            );
            assertTrue(ex.getErrors().size() >= 3);
        }
    }

    @Nested
    @DisplayName("Reconstituição (with)")
    class With {

        @Test
        @DisplayName("deve reconstituir usuário a partir de dados existentes")
        void shouldReconstitute() {
            final var id = UserID.unique();
            final var now = Instant.now();
            final var user = User.with(id, "Maria", "maria", password, cpf, "41988888888",
                    cityID, "80001-000", "Av. Brasil", "456", "CRM-999", "2015",
                    now, now, true, Set.of());

            assertEquals(id, user.getId());
            assertEquals("Maria", user.getName());
            assertTrue(user.isActive());
        }
    }

    @Nested
    @DisplayName("Atualização")
    class Update {

        @Test
        @DisplayName("deve atualizar campos mutáveis do usuário")
        void shouldUpdateMutableFields() {
            final var user = validUser();
            final var newCity = CityID.unique();
            final var newPwd = Password.from("Nova@Senha1", "Nova@Senha1");

            user.update("41977777777", newCity, "81000-000", "Rua Nova", "999",
                    "CREA-99999", "2020", newPwd, true, Set.of());

            assertEquals("41977777777", user.getPhone());
            assertEquals(newCity, user.getCityID());
            assertTrue(user.isActive());
        }

    }

    @Nested
    @DisplayName("Permissões")
    class Permissions {

        @Test
        @DisplayName("deve criar usuário com permissões")
        void shouldCreateWithPermissions() {
            final var perm = Permission.create(UserRole.TECNICO, true, Set.of(), Set.of());
            final var user = User.create("Ana", "ana", password, cpf, null, null, null, null, null, null, null, Set.of(perm));

            assertEquals(1, user.getPermissions().size());
        }

        @Test
        @DisplayName("deve criar usuário sem permissões")
        void shouldCreateWithNoPermissions() {
            final var user = validUser();
            assertNotNull(user.getPermissions());
            assertTrue(user.getPermissions().isEmpty());
        }
    }
}
