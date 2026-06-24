package br.gov.pr.idr.infra.iam.security.config;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

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
}
