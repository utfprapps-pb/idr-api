package br.gov.pr.idr.infra.iam.security.jwt;

import br.gov.pr.idr.infra.iam.security.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService")
class JwtServiceTest {

    @Mock UserDetails userDetails;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        final var props = new JwtProperties(
                "test-secret-for-hmac384-algorithm-must-be-long-enough",
                3_600_000L,
                7L
        );
        jwtService = new JwtService(props);
    }

    @Test
    @DisplayName("deve gerar token não vazio para usuário válido")
    void shouldGenerateNonEmptyToken() {
        when(userDetails.getUsername()).thenReturn("testuser");

        final var token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("deve extrair username correto do token gerado")
    void shouldExtractCorrectUsername() {
        when(userDetails.getUsername()).thenReturn("joao.silva");
        final var token = jwtService.generateToken(userDetails);

        final var username = jwtService.extractUsername(token);

        assertEquals("joao.silva", username);
    }

    @Test
    @DisplayName("deve retornar null para token inválido")
    void shouldReturnNullForInvalidToken() {
        final var username = jwtService.extractUsername("token.invalido.aqui");

        assertNull(username);
    }
}
