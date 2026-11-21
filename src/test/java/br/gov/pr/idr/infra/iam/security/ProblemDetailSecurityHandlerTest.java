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
    @DisplayName("AuthenticationFailureHandler repassa a falha de login")
    void onAuthenticationFailureDelegatesToResolver() throws Exception {
        final var ex = new BadCredentialsException("Bad credentials");

        handler.onAuthenticationFailure(request, response, ex);

        verify(resolver).resolveException(org.mockito.ArgumentMatchers.eq(request),
                org.mockito.ArgumentMatchers.eq(response), isNull(), org.mockito.ArgumentMatchers.eq(ex));
    }
}
