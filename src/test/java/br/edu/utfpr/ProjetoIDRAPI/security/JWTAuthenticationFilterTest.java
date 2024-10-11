package br.edu.utfpr.ProjetoIDRAPI.security;

import br.edu.utfpr.ProjetoIDRAPI.TestUtils;
import br.edu.utfpr.ProjetoIDRAPI.entity.token.AuthService;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTAuthenticationFilterTest {

    private JWTAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationManager authenticationManager;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        authService = mock(AuthService.class);
        jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, authService);
    }

    @Test
    void testAttemptAuthentication() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent("{\"username\":\"testuser\",\"password\":\"password\"}".getBytes());

        User user = TestUtils.createUser("testuser", "password");
        when(authService.loadUserByUsername("testuser")).thenReturn(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("testuser", "password", Collections.emptyList());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authToken);

        Authentication authentication = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertEquals("testuser", authentication.getName());
        assertEquals("password", authentication.getCredentials());
    }

    @Test
    void testAttemptAuthenticationIOException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setContent("invalid_json".getBytes());
        assertThrows(RuntimeException.class, () -> jwtAuthenticationFilter.attemptAuthentication(request, response));
    }

    @Test
    void testSuccessfulAuthentication() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        Authentication authResult = mock(Authentication.class);
        when(authResult.getName()).thenReturn("testuser");

        User user = TestUtils.createUser("testuser", "password");
        when(authService.loadUserByUsername("testuser")).thenReturn(user);

        jwtAuthenticationFilter.successfulAuthentication(request, response, filterChain, authResult);
        String token = JWT.create()
                .withSubject("testuser")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET));

        String jsonResponse = response.getContentAsString();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("{\"token\":\"" + token + "\",\"user\":{\"username\":\"testuser\",\"displayName\":null,\"authorities\":[]}}", jsonResponse);
    }
}