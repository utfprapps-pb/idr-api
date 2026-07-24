package br.gov.pr.idr.infra.iam.security.jwt;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenOutput;
import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter")
class JwtAuthenticationFilterTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock JwtService jwtService;
    @Mock IssueRefreshTokenUseCase issueRefreshTokenUseCase;

    private JwtAuthenticationFilter filter;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(authenticationManager, jwtService,
                objectMapper, issueRefreshTokenUseCase, 7L);
        filter.setAuthenticationManager(authenticationManager);
    }

    @Test
    @DisplayName("attemptAuthentication deve parsear credenciais e chamar authManager")
    void shouldAttemptAuthentication() throws Exception {
        final var request = new MockHttpServletRequest();
        request.setContent("{\"username\":\"joao\",\"password\":\"Senha@123\"}".getBytes());
        final var response = new MockHttpServletResponse();
        final var authToken = new UsernamePasswordAuthenticationToken("joao", "Senha@123");
        when(authenticationManager.authenticate(any())).thenReturn(authToken);

        final var result = filter.attemptAuthentication(request, response);

        assertNotNull(result);
        verify(authenticationManager).authenticate(any());
    }

    @Test
    @DisplayName("attemptAuthentication com corpo JSON inválido deve propagar exceção")
    void shouldPropagateExceptionWhenBodyIsMalformed() {
        final var request = new MockHttpServletRequest();
        request.setContent("{invalid-json".getBytes());
        final var response = new MockHttpServletResponse();

        assertThrows(Exception.class, () -> filter.attemptAuthentication(request, response));
        verifyNoInteractions(authenticationManager);
    }

    @Test
    @DisplayName("unsuccessfulAuthentication com DisabledException deve retornar 403")
    void shouldReturn403WhenDisabled() throws Exception {
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();
        final var ex = new DisabledException("conta inativa");

        filter.unsuccessfulAuthentication(request, response, ex);

        assertEquals(403, response.getStatus());
        assertTrue(response.getContentAsString().contains("inativo"));
    }

    @Test
    @DisplayName("unsuccessfulAuthentication com outra exceção deve delegar ao super")
    void shouldDelegateToSuperForOtherExceptions() throws Exception {
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();
        final var ex = new BadCredentialsException("credenciais inválidas");

        filter.unsuccessfulAuthentication(request, response, ex);

        assertEquals(401, response.getStatus());
    }

    @Test
    @DisplayName("unsuccessfulAuthentication com DisabledException deve propagar exceção quando resposta falha ao escrever")
    void shouldPropagateExceptionWhenResponseWriterFailsOnDisabled() throws Exception {
        final var request = new MockHttpServletRequest();
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenThrow(new IOException("falha ao obter writer"));
        final var ex = new DisabledException("conta inativa");

        assertThrows(Exception.class, () -> filter.unsuccessfulAuthentication(request, response, ex));
        verify(response).setStatus(403);
    }

    @Test
    @DisplayName("successfulAuthentication deve escrever access e refresh token na resposta")
    void shouldWriteTokensOnSuccess() throws Exception {
        final var userId = UUID.randomUUID();
        final var authority = new SimpleGrantedAuthority("TECNICO");
        final var userDetails = mock(br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity.class);
        when(userDetails.getId()).thenReturn(userId);
        when(userDetails.getUsername()).thenReturn("joao.silva");

        final var authResult = new UsernamePasswordAuthenticationToken(
                userDetails, null, List.of(authority));

        when(jwtService.generateToken(userDetails)).thenReturn("access-token-value");
        when(issueRefreshTokenUseCase.execute(any())).thenReturn(new IssueRefreshTokenOutput("refresh-token-value"));

        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();

        filter.successfulAuthentication(request, response, chain, authResult);

        final var body = response.getContentAsString();
        assertTrue(body.contains("access-token-value"));
        assertTrue(body.contains("refresh-token-value"));
    }

    @Test
    @DisplayName("successfulAuthentication deve lançar AssertionError quando principal autenticado é nulo")
    void shouldThrowAssertionErrorWhenPrincipalIsNull() {
        final var authResult = new UsernamePasswordAuthenticationToken(null, null);
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();

        assertThrows(AssertionError.class,
                () -> filter.successfulAuthentication(request, response, chain, authResult));
        verifyNoInteractions(jwtService, issueRefreshTokenUseCase);
    }

    @Test
    @DisplayName("successfulAuthentication deve propagar exceção quando resposta falha ao escrever tokens")
    void shouldPropagateExceptionWhenResponseWriterFailsOnSuccess() throws Exception {
        final var userDetails = mock(br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity.class);
        when(userDetails.getUsername()).thenReturn("joao.silva");
        final var authResult = new UsernamePasswordAuthenticationToken(
                userDetails, null, List.of(new SimpleGrantedAuthority("TECNICO")));
        when(jwtService.generateToken(userDetails)).thenReturn("access-token-value");
        when(issueRefreshTokenUseCase.execute(any())).thenReturn(new IssueRefreshTokenOutput("refresh-token-value"));

        final var request = new MockHttpServletRequest();
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenThrow(new IOException("falha ao obter writer"));
        final var chain = new MockFilterChain();

        assertThrows(Exception.class,
                () -> filter.successfulAuthentication(request, response, chain, authResult));
    }
}
