package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.user.create.CreateUserOutput;
import br.gov.pr.idr.application.iam.user.create.CreateUserUseCase;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByIdOutput;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByIdUseCase;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameOutput;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameUseCase;
import br.gov.pr.idr.application.iam.user.retries.permissions.GetUserPermissionsOutput;
import br.gov.pr.idr.application.iam.user.retries.permissions.GetUserPermissionsUseCase;
import br.gov.pr.idr.application.iam.user.retries.search.SearchUserOutput;
import br.gov.pr.idr.application.iam.user.retries.search.SearchUserUseCase;
import br.gov.pr.idr.application.iam.user.update.ToggleUserActiveUseCase;
import br.gov.pr.idr.application.iam.user.update.UpdateUserPermissionsUseCase;
import br.gov.pr.idr.domain.iam.user.UserRole;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController")
class UserControllerTest {

    @Mock CreateUserUseCase createUserUseCase;
    @Mock SearchUserUseCase searchUserUseCase;
    @Mock FindUserByUsernameUseCase findUserByUsernameUseCase;
    @Mock FindUserByIdUseCase findUserByIdUseCase;
    @Mock GetUserPermissionsUseCase getUserPermissionsUseCase;
    @Mock UpdateUserPermissionsUseCase updateUserPermissionsUseCase;
    @Mock ToggleUserActiveUseCase toggleUserActiveUseCase;
    @InjectMocks UserController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/users deve criar usuário e retornar 200")
    void shouldCreateUser() throws Exception {
        final var cityId = UUID.randomUUID();
        final var output = new CreateUserOutput(UUID.randomUUID(), "João Silva");
        when(createUserUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"João Silva\",\"username\":\"joao.silva\"," +
                                "\"password\":\"Senha@123\",\"confirmPassword\":\"Senha@123\"," +
                                "\"cpf\":\"529.982.247-25\",\"phone\":\"41999999999\"," +
                                "\"graduationYear\":\"2010\",\"professionalRegister\":\"CREA-1234\"," +
                                "\"cep\":\"80000-000\",\"street\":\"Rua das Flores\"," +
                                "\"cityId\":\"" + cityId + "\",\"houseNumber\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    @DisplayName("GET /v1/users/me sem autenticação deve retornar 204")
    void shouldReturnNoContentWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/v1/users/me"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /v1/users/me com autenticação deve retornar 200 com nome e role")
    void shouldReturnUserWhenAuthenticated() {
        final var auth = new UsernamePasswordAuthenticationToken(
                "joao.silva", null,
                List.of(new SimpleGrantedAuthority("TECNICO")));
        final var output = new FindUserByUsernameOutput(UUID.randomUUID(), "João Silva");
        when(findUserByUsernameUseCase.execute(any())).thenReturn(output);

        final var response = controller.me(auth);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("João Silva", response.getBody().displayName());
        assertEquals("TECNICO", response.getBody().role());
    }

    @Test
    @DisplayName("GET /v1/users/me com autenticação sem principal deve retornar 204")
    void shouldReturnNoContentWhenPrincipalIsNull() {
        final var auth = new UsernamePasswordAuthenticationToken(null, null);

        final var response = controller.me(auth);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    @DisplayName("GET /v1/users/search deve retornar paginação")
    void shouldSearchUsers() throws Exception {
        final var output = new SearchUserOutput(UUID.randomUUID(), "João", "joao",
                "41999999999", "CREA-1234", "2010", UUID.randomUUID(), true, Instant.now(), null);
        final var pagination = new Pagination<>(0, 10, 1L, List.of(output));
        when(searchUserUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/v1/users/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @DisplayName("GET /v1/users/{id} deve retornar usuário por ID")
    void shouldFindUserById() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new FindUserByIdOutput(id, "João", "joao",
                "529.982.247-25", "41999999999", UUID.randomUUID(),
                true, Instant.now(), null, false, Set.of(), Set.of());
        when(findUserByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get("/v1/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"));
    }

    @Test
    @DisplayName("GET /v1/users/{id}/permissions deve retornar permissões do usuário")
    void shouldGetUserPermissions() throws Exception {
        final var id = UUID.randomUUID();
        final var permissionId = UUID.randomUUID();
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new GetUserPermissionsOutput(id, List.of(
                new GetUserPermissionsOutput.PermissionItem(
                        permissionId, UserRole.TECNICO, true, Set.of(regionId), Set.of(cityId))));
        when(getUserPermissionsUseCase.execute(id)).thenReturn(output);

        mockMvc.perform(get("/v1/users/" + id + "/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id.toString()))
                .andExpect(jsonPath("$.permissions[0].role").value("TECNICO"))
                .andExpect(jsonPath("$.permissions[0].readOnly").value(true));
    }

    @Test
    @DisplayName("PUT /v1/users/{id}/permissions deve atualizar permissões e retornar 204")
    void shouldUpdatePermissions() throws Exception {
        final var id = UUID.randomUUID();
        doNothing().when(updateUserPermissionsUseCase).execute(any());

        mockMvc.perform(put("/v1/users/" + id + "/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"TECNICO\",\"readOnly\":false,\"regionIds\":[],\"cityIds\":[]}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /v1/users/{id}/active deve alternar ativo e retornar 204")
    void shouldToggleActive() throws Exception {
        final var id = UUID.randomUUID();
        doNothing().when(toggleUserActiveUseCase).execute(any());

        mockMvc.perform(patch("/v1/users/" + id + "/active"))
                .andExpect(status().isNoContent());
    }
}
