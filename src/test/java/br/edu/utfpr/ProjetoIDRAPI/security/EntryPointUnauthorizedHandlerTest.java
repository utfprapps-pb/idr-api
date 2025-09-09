package br.edu.utfpr.ProjetoIDRAPI.security;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EntryPointUnauthorizedHandlerTest {

    private EntryPointUnauthorizedHandler unauthorizedHandler;

    @BeforeEach
    void setUp() {
        unauthorizedHandler = new EntryPointUnauthorizedHandler();
    }

    @Test
    void testCommenceSetsUnauthorizedStatus() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = Mockito.mock(AuthenticationException.class);

        unauthorizedHandler.commence(request, response, authException);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(HttpStatus.UNAUTHORIZED.getReasonPhrase(), response.getErrorMessage());
    }
}