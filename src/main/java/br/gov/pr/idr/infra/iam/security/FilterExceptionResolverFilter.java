package br.gov.pr.idr.infra.iam.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Primeiro filtro da cadeia de segurança: uma exceção lançada nos filtros seguintes (corpo inválido no login,
 * banco fora do ar ao carregar o usuário do token) não chega ao {@code @RestControllerAdvice} e cairia no
 * {@code /error} do Boot. Aqui ela é repassada ao {@link HandlerExceptionResolver} do MVC, para que o
 * {@code GlobalExceptionHandler} responda no formato Problem Details (RFC 9457).
 */
public class FilterExceptionResolverFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    public FilterExceptionResolverFilter(final HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            if (response.isCommitted() || resolver.resolveException(request, response, null, ex) == null) {
                throw ex;
            }
        }
    }
}
