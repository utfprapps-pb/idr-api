package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.infra.iam.security.config.JwtProperties;
import br.gov.pr.idr.infra.shared.testing.ApiIntegrationTest;
import br.gov.pr.idr.infra.shared.testing.ApiTestSupport;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Status HTTP de autenticação (401) versus autorização (403) com o {@code JwtService} real: o frontend só renova o
 * token ao receber 401, então requisição sem token ou com token inválido não pode responder 403.
 */
@ApiIntegrationTest
@DisplayName("Autenticação: 401 para não autenticado, 403 para sem permissão — Postgres real")
class AuthenticationStatusApiTest extends ApiTestSupport {

    private static final String PROTECTED = "/v1/users/me";
    private static final String PROPERTY = "c0000000-0000-0000-0000-000000000001";
    private static final String MIMOSA = "/properties/" + PROPERTY + "/animals/e0000000-0000-0000-0000-000000000001";
    private static final String INVALID_REFRESH = "{\"token\":\"refresh-token-inexistente\"}";

    @Autowired private JwtProperties jwtProperties;
    @Autowired private PasswordEncoder passwordEncoder;

    private String expiredToken() {
        return JWT.create()
                .withSubject(TECHNICIAN)
                .withExpiresAt(new Date(System.currentTimeMillis() - 60_000))
                .sign(Algorithm.HMAC384(jwtProperties.secret()));
    }

    private static String tokenSignedWithAnotherSecret() {
        return JWT.create()
                .withSubject(TECHNICIAN)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60_000))
                .sign(Algorithm.HMAC384("outro-segredo-que-nao-e-o-da-aplicacao-32chars"));
    }

    /** Corpo de erro no formato Problem Details (RFC 9457) com o type esperado. */
    private static ResultMatcher problem(final String type) {
        return result -> {
            content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON).match(result);
            jsonPath("$.type").value("urn:idr:problem:" + type).match(result);
        };
    }

    private String tokenOfRemovedUser() {
        return JWT.create()
                .withSubject("removido@idr.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60_000))
                .sign(Algorithm.HMAC384(jwtProperties.secret()));
    }

    private static MockHttpServletRequestBuilder withBearer(final MockHttpServletRequestBuilder request,
                                                            final String token) {
        return request.header("Authorization", "Bearer " + token);
    }

    @Test
    @DisplayName("rota protegida sem Authorization retorna 401")
    void missingTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get(PROTECTED)).andExpect(status().isUnauthorized()).andExpect(problem("nao-autenticado"));
        mockMvc.perform(get(MIMOSA)).andExpect(status().isUnauthorized()).andExpect(problem("nao-autenticado"));
    }

    @Test
    @DisplayName("token expirado retorna 401")
    void expiredTokenIsUnauthorized() throws Exception {
        mockMvc.perform(withBearer(get(PROTECTED), expiredToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(problem("nao-autenticado"));
    }

    @Test
    @DisplayName("token com assinatura inválida retorna 401")
    void tokenWithInvalidSignatureIsUnauthorized() throws Exception {
        mockMvc.perform(withBearer(get(PROTECTED), tokenSignedWithAnotherSecret()))
                .andExpect(status().isUnauthorized())
                .andExpect(problem("nao-autenticado"));
    }

    @Test
    @DisplayName("token malformado retorna 401")
    void malformedTokenIsUnauthorized() throws Exception {
        mockMvc.perform(withBearer(get(PROTECTED), "nao-e-um-jwt"))
                .andExpect(status().isUnauthorized())
                .andExpect(problem("nao-autenticado"));
    }

    @Test
    @DisplayName("token válido acessa a rota protegida")
    void validTokenIsAccepted() throws Exception {
        perform(get(PROTECTED)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("técnico autenticado em rota ADMIN (GET /v1/users/{id}) continua 403")
    void authenticatedWithoutAuthorityIsForbidden() throws Exception {
        perform(get("/v1/users/a0000000-0000-0000-0000-000000000001"))
                .andExpect(status().isForbidden())
                .andExpect(problem("acesso-negado"));
    }

    @Test
    @DisplayName("técnico desvinculado da propriedade continua 403 (ForbiddenException do domínio)")
    void propertyOutOfScopeIsForbidden() throws Exception {
        sql("delete from property_technician where property_id = '" + PROPERTY + "'");

        perform(get(MIMOSA)).andExpect(status().isForbidden()).andExpect(problem("acesso-negado"));
    }

    @Test
    @DisplayName("login com senha errada retorna 401")
    void loginWithWrongPasswordIsUnauthorized() throws Exception {
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + TECHNICIAN + "\",\"password\":\"senha-errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(problem("credenciais-invalidas"))
                .andExpect(jsonPath("$.detail").value("Usuário ou senha inválidos"))
                .andExpect(jsonPath("$.instance").value("/login"));
    }

    @Test
    @DisplayName("login de usuário inativo (senha correta) continua 403")
    void loginOfInactiveUserIsForbidden() throws Exception {
        // Usuário novo: o técnico do seed já está no contexto de persistência (carregado pelo ApiTestSupport).
        sql("insert into users (id, active, cep, city_id, cpf, name, graduation_year, house_number, password, phone, "
                + "professional_register, street, username, created_at, updated_at) values "
                + "('a0000000-0000-0000-0000-000000000901', false, '0000', '58ac2f17-d95a-457f-85a9-3db07f3f50e6', "
                + "'123.456.781-43', 'Inativo', '2000', '1', '" + passwordEncoder.encode("Senha@123") + "', '0', '0', "
                + "'Rua', 'inativo@idr.com', NOW(), NOW())");

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"inativo@idr.com\",\"password\":\"Senha@123\"}"))
                .andExpect(status().isForbidden())
                .andExpect(problem("cadastro-inativo"))
                .andExpect(jsonPath("$.detail")
                        .value("Seu cadastro está inativo, solicite a um administrador para ativá-lo"));
    }

    @Test
    @DisplayName("POST /v1/auth/refresh é público: sem token chega ao controller (refresh inválido → 400)")
    void refreshIsPublicWithoutToken() throws Exception {
        mockMvc.perform(post("/v1/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(INVALID_REFRESH))
                .andExpect(status().isBadRequest())
                .andExpect(problem("refresh-token-invalido"));
    }

    @Test
    @DisplayName("POST /v1/auth/refresh com access token expirado no header também chega ao controller")
    void refreshIsPublicWithExpiredAccessToken() throws Exception {
        mockMvc.perform(withBearer(post("/v1/auth/refresh"), expiredToken())
                        .contentType(MediaType.APPLICATION_JSON).content(INVALID_REFRESH))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("login com corpo inválido: 400 validacao, não o /error do Boot")
    void loginWithMalformedBodyIsValidation() throws Exception {
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(problem("validacao"))
                .andExpect(jsonPath("$.detail").value("Corpo da requisição ausente ou inválido"));
    }

    @Test
    @DisplayName("token válido de usuário removido do banco retorna 401, não 500")
    void removedUserTokenIsUnauthorized() throws Exception {
        mockMvc.perform(withBearer(get(PROTECTED), tokenOfRemovedUser()))
                .andExpect(status().isUnauthorized())
                .andExpect(problem("nao-autenticado"));
    }

    @Test
    @DisplayName("UUID inválido no path: 400 validacao com texto do messages.properties")
    void invalidUuidInPathUsesPortugueseMessages() throws Exception {
        perform(get("/properties/abc/animals/search"))
                .andExpect(status().isBadRequest())
                .andExpect(problem("validacao"))
                .andExpect(jsonPath("$.detail").value(startsWith("Valor inválido para o parâmetro")));
    }

    @Test
    @DisplayName("rota inexistente com token: 404 com título em português")
    void unknownRouteIs404() throws Exception {
        perform(get("/v1/rota-que-nao-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Rota não encontrada"));
    }

    @Test
    @DisplayName("rota permitAll (GET /v1/cities/search) é acessível sem token")
    void permitAllRouteIsAccessibleWithoutToken() throws Exception {
        mockMvc.perform(get("/v1/cities/search")).andExpect(status().isOk());
    }
}
