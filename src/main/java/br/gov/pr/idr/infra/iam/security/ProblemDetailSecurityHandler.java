package br.gov.pr.idr.infra.iam.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

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
                         final AuthenticationException authException) throws IOException {
        resolve(request, response, authException, HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        resolve(request, response, accessDeniedException, HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException {
        resolve(request, response, exception, HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * Se o MVC não conseguir escrever o corpo (ex.: {@code Accept} que não aceita JSON), o resolver devolve
     * {@code null}; sem este fallback a resposta sairia 200 vazia.
     */
    private void resolve(final HttpServletRequest request, final HttpServletResponse response, final Exception ex,
                         final int fallbackStatus) throws IOException {
        if (resolver.resolveException(request, response, null, ex) == null && !response.isCommitted()) {
            response.sendError(fallbackStatus);
        }
    }
}
