package br.gov.pr.idr.infra.iam.security.config;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecurityConfig.class, SecurityConfigFilterChainTest.TestBeans.class})
@WebAppConfiguration
@DisplayName("SecurityConfig — filterChain")
class SecurityConfigFilterChainTest {

    static class TestBeans {
        @Bean JwtService jwtService() { return mock(JwtService.class); }
        @Bean JwtProperties jwtProperties() {
            return new JwtProperties("test-secret-hmac384-must-be-long-enough-32chars", 3_600_000L, 7L);
        }
        @Bean UserDetailsService userDetailsService() { return mock(UserDetailsService.class); }
        @Bean IssueRefreshTokenUseCase issueRefreshTokenUseCase() { return mock(IssueRefreshTokenUseCase.class); }
    }

    @Autowired WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("POST /v1/users deve ser público (sem autenticação → não retorna 401)")
    void postUsersIsPublic() throws Exception {
        mockMvc.perform(post("/v1/users").contentType("application/json").content("{}"))
                .andExpect(status().is(not(401)));
    }

    @Test
    @DisplayName("POST /v1/auth/refresh deve ser público")
    void postAuthRefreshIsPublic() throws Exception {
        mockMvc.perform(post("/v1/auth/refresh").contentType("application/json").content("{}"))
                .andExpect(status().is(not(401)));
    }

    @Test
    @DisplayName("GET /v1/cities/search deve ser público")
    void getCitiesSearchIsPublic() throws Exception {
        mockMvc.perform(get("/v1/cities/search"))
                .andExpect(status().is(not(401)));
    }

    @Test
    @DisplayName("POST /v1/email/send-recuperation-code deve ser público")
    void postEmailSendCodeIsPublic() throws Exception {
        mockMvc.perform(post("/v1/email/send-recuperation-code").contentType("application/json").content("{}"))
                .andExpect(status().is(not(401)));
    }

    @Test
    @DisplayName("GET /v1/users/me deve exigir autenticação (retorna 401 sem token)")
    void getUsersMeRequiresAuth() throws Exception {
        mockMvc.perform(get("/v1/users/me"))
                .andExpect(status().is(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(401), org.hamcrest.Matchers.is(403))));
    }

    @Test
    @DisplayName("GET /v1/users/search deve exigir autenticação")
    void getUsersSearchRequiresAuth() throws Exception {
        mockMvc.perform(get("/v1/users/search"))
                .andExpect(status().is(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(401), org.hamcrest.Matchers.is(403))));
    }

    @Test
    @DisplayName("GET /v1/users/{id} deve exigir authority ADMIN (retorna 401 sem token)")
    void getUserByIdRequiresAdmin() throws Exception {
        mockMvc.perform(get("/v1/users/" + java.util.UUID.randomUUID()))
                .andExpect(status().is(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(401), org.hamcrest.Matchers.is(403))));
    }

    @Test
    @DisplayName("DELETE /v1/properties/{id} deve exigir autenticação")
    void deletePropertyRequiresAuth() throws Exception {
        mockMvc.perform(delete("/v1/properties/" + java.util.UUID.randomUUID()))
                .andExpect(status().is(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(401), org.hamcrest.Matchers.is(403))));
    }
}
