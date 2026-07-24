package br.gov.pr.idr.infra.iam.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthorizationFilter")
class JwtAuthorizationFilterTest {

    @Mock JwtService jwtService;
    @Mock UserDetailsService userDetailsService;
    @InjectMocks JwtAuthorizationFilter filter;

    @BeforeEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("deve deixar passar quando não há cabeçalho Authorization")
    void shouldPassThroughWhenNoAuthorizationHeader() throws Exception {
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertNotNull(chain.getRequest());
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("deve deixar passar quando cabeçalho não começa com Bearer")
    void shouldPassThroughWhenHeaderNotBearer() throws Exception {
        final var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertNotNull(chain.getRequest());
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("deve definir autenticação quando token válido e username resolvido")
    void shouldSetAuthenticationWhenValidToken() throws Exception {
        final var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid.token.here");
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();
        final var userDetails = new User("joao.silva", "pwd",
                List.of(new SimpleGrantedAuthority("TECNICO")));

        when(jwtService.extractUsername("valid.token.here")).thenReturn("joao.silva");
        when(userDetailsService.loadUserByUsername("joao.silva")).thenReturn(userDetails);

        filter.doFilterInternal(request, response, chain);

        final var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("joao.silva", auth.getPrincipal());
    }

    @Test
    @DisplayName("deve deixar passar sem setar autenticação quando username é nulo")
    void shouldPassThroughWhenUsernameIsNull() throws Exception {
        final var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid.token");
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();

        when(jwtService.extractUsername("invalid.token")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("deve deixar passar sem sobrescrever autenticação quando já existe autenticação no contexto")
    void shouldNotOverrideAuthenticationWhenAlreadyAuthenticated() throws Exception {
        final var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid.token.here");
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();
        final var existingAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "maria.souza", null, List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        when(jwtService.extractUsername("valid.token.here")).thenReturn("joao.silva");

        filter.doFilterInternal(request, response, chain);

        final var auth = SecurityContextHolder.getContext().getAuthentication();
        assertSame(existingAuth, auth);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("deve lançar NullPointerException quando request é nulo")
    void shouldThrowWhenRequestIsNull() {
        final var response = new MockHttpServletResponse();
        final var chain = new MockFilterChain();

        assertThrows(NullPointerException.class, () -> filter.doFilterInternal(null, response, chain));
    }

    @Test
    @DisplayName("deve lançar NullPointerException quando response é nulo")
    void shouldThrowWhenResponseIsNull() {
        final var request = new MockHttpServletRequest();
        final var chain = new MockFilterChain();

        assertThrows(NullPointerException.class, () -> filter.doFilterInternal(request, null, chain));
    }

    @Test
    @DisplayName("deve lançar NullPointerException quando filterChain é nulo")
    void shouldThrowWhenFilterChainIsNull() {
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        assertThrows(NullPointerException.class, () -> filter.doFilterInternal(request, response, null));
    }
}
