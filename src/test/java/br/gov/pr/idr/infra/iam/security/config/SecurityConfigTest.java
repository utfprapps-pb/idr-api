package br.gov.pr.idr.infra.iam.security.config;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig")
class SecurityConfigTest {

    @Mock JwtService jwtService;
    @Mock JwtProperties jwtProperties;
    @Mock UserDetailsService userDetailsService;
    @Mock IssueRefreshTokenUseCase issueRefreshTokenUseCase;

    private SecurityConfig config;

    @BeforeEach
    void setUp() {
        config = new SecurityConfig(jwtService, jwtProperties, userDetailsService, issueRefreshTokenUseCase);
    }

    @Test
    @DisplayName("objectMapper deve registrar JavaTimeModule e não escrever datas como timestamps")
    void shouldConfigureObjectMapper() throws Exception {
        final var mapper = config.objectMapper();
        final var result = mapper.writeValueAsString(Instant.EPOCH);

        assertNotNull(mapper);
        assertTrue(result.contains("1970"), "Deve serializar Instant como ISO string, não timestamp");
    }

    @Test
    @DisplayName("passwordEncoder deve ser BCryptPasswordEncoder capaz de codificar e verificar senhas")
    void shouldProvideBCryptPasswordEncoder() {
        final var encoder = config.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
        final var encoded = encoder.encode("Senha@123");
        assertTrue(encoder.matches("Senha@123", encoded));
        assertFalse(encoder.matches("outra-senha", encoded));
    }

    @Test
    @DisplayName("filterChain deve propagar exceção quando HttpSecurity é nulo")
    void shouldPropagateExceptionWhenHttpSecurityIsNull() {
        assertThrows(Exception.class, () -> config.filterChain(null));
    }

    @Test
    @DisplayName("corsConfigurationSource deve configurar origens, métodos e cabeçalhos permitidos")
    void shouldConfigureCors() {
        final var source = (UrlBasedCorsConfigurationSource) config.corsConfigurationSource();
        final var cors = source.getCorsConfigurations().get("/**");

        assertNotNull(cors);
        assertNotNull(cors.getAllowedOrigins());
        assertTrue(cors.getAllowedOrigins().contains("*"));
        assertTrue(cors.getAllowedMethods().containsAll(
                java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")));
        assertTrue(cors.getAllowedHeaders().contains("Authorization"));
        assertTrue(cors.getAllowedHeaders().contains("Content-Type"));
    }

    @Nested
    @ExtendWith(SpringExtension.class)
    @ContextConfiguration(classes = {SecurityConfig.class, FilterChainTests.TestBeans.class})
    @WebAppConfiguration
    @DisplayName("filterChain")
    class FilterChainTests {

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
        void setUpMockMvc() {
            mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                    .apply(springSecurity())
                    .build();
        }

        @Test
        @DisplayName("deve construir a cadeia de filtros permitindo rota pública e exigindo autenticação em rota protegida")
        void shouldBuildFilterChainWithConfiguredRules() throws Exception {
            mockMvc.perform(post("/v1/users").contentType("application/json").content("{}"))
                    .andExpect(status().is(not(401)));

            mockMvc.perform(get("/v1/users/me"))
                    .andExpect(status().is(org.hamcrest.Matchers.anyOf(
                            org.hamcrest.Matchers.is(401), org.hamcrest.Matchers.is(403))));
        }
    }
}
