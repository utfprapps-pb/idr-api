package br.gov.pr.idr.infra.iam.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("ProblemDetailSecurityHandler: repassa os erros de segurança ao GlobalExceptionHandler")
class ProblemDetailSecurityHandlerTest {

    private final HandlerExceptionResolver resolver = mock(HandlerExceptionResolver.class);
    private final ProblemDetailSecurityHandler handler = new ProblemDetailSecurityHandler(resolver);
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    @DisplayName("entry point repassa a AuthenticationException")
    void commenceDelegatesToResolver() throws Exception {
        final var ex = new InsufficientAuthenticationException("Full authentication is required");

        handler.commence(request, response, ex);

        verify(resolver).resolveException(org.mockito.ArgumentMatchers.eq(request),
                org.mockito.ArgumentMatchers.eq(response), isNull(), org.mockito.ArgumentMatchers.eq(ex));
    }

    @Test
    @DisplayName("AccessDeniedHandler repassa a AccessDeniedException")
    void handleDelegatesToResolver() throws Exception {
        final var ex = new AccessDeniedException("Access Denied");

        handler.handle(request, response, ex);

        verify(resolver).resolveException(org.mockito.ArgumentMatchers.eq(request),
                org.mockito.ArgumentMatchers.eq(response), isNull(), org.mockito.ArgumentMatchers.eq(ex));
    }

    @Test
    @DisplayName("resolver sem resposta (ex.: Accept não suportado): entry point ainda responde 401, não 200 vazio")
    void commenceFallsBackTo401() throws Exception {
        handler.commence(request, response, new InsufficientAuthenticationException("x"));

        org.junit.jupiter.api.Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    @DisplayName("resolver sem resposta: AccessDeniedHandler ainda responde 403")
    void handleFallsBackTo403() throws Exception {
        handler.handle(request, response, new AccessDeniedException("x"));

        org.junit.jupiter.api.Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    @DisplayName("resolver sem resposta: falha de login ainda responde 401")
    void onAuthenticationFailureFallsBackTo401() throws Exception {
        handler.onAuthenticationFailure(request, response, new BadCredentialsException("x"));

        org.junit.jupiter.api.Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    @DisplayName("AuthenticationFailureHandler repassa a falha de login")
    void onAuthenticationFailureDelegatesToResolver() throws Exception {
        final var ex = new BadCredentialsException("Bad credentials");

        handler.onAuthenticationFailure(request, response, ex);

        verify(resolver).resolveException(org.mockito.ArgumentMatchers.eq(request),
                org.mockito.ArgumentMatchers.eq(response), isNull(), org.mockito.ArgumentMatchers.eq(ex));
    }
}
