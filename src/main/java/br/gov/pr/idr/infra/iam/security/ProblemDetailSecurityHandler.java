package br.gov.pr.idr.infra.iam.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Os erros do Spring Security acontecem nos filtros, fora do alcance do {@code @RestControllerAdvice}. Esta classe
 * os repassa ao {@link HandlerExceptionResolver} do MVC, para que o {@code GlobalExceptionHandler} responda a
 * todos no mesmo formato Problem Details (RFC 9457).
 */
public class ProblemDetailSecurityHandler
        implements AuthenticationEntryPoint, AccessDeniedHandler, AuthenticationFailureHandler {

    private final HandlerExceptionResolver resolver;

    public ProblemDetailSecurityHandler(final HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) {
        resolver.resolveException(request, response, null, authException);
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) {
        resolver.resolveException(request, response, null, accessDeniedException);
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) {
        resolver.resolveException(request, response, null, exception);
    }
}
