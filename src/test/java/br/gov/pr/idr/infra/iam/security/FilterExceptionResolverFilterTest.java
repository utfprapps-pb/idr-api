package br.gov.pr.idr.infra.iam.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("FilterExceptionResolverFilter: exceções dos filtros vão para o GlobalExceptionHandler")
class FilterExceptionResolverFilterTest {

    private final HandlerExceptionResolver resolver = mock(HandlerExceptionResolver.class);
    private final FilterExceptionResolverFilter filter = new FilterExceptionResolverFilter(resolver);
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final FilterChain chain = mock(FilterChain.class);

    @Test
    @DisplayName("exceção de um filtro posterior é resolvida e não sobe ao container")
    void exceptionIsResolved() throws Exception {
        final var ex = new IllegalStateException("banco fora do ar");
        doThrow(ex).when(chain).doFilter(any(), any());
        when(resolver.resolveException(eq(request), eq(response), isNull(), eq(ex))).thenReturn(new ModelAndView());

        filter.doFilter(request, response, chain);

        verify(resolver).resolveException(eq(request), eq(response), isNull(), eq(ex));
    }

    @Test
    @DisplayName("exceção que o resolver não trata é relançada")
    void unresolvedExceptionIsRethrown() throws Exception {
        final var ex = new IllegalStateException("x");
        doThrow(ex).when(chain).doFilter(any(), any());

        final var thrown = assertThrows(IllegalStateException.class, () -> filter.doFilter(request, response, chain));

        assertSame(ex, thrown);
    }

    @Test
    @DisplayName("resposta já enviada: relança sem tentar escrever outro corpo")
    void committedResponseIsNotRewritten() throws Exception {
        final var ex = new IllegalStateException("x");
        doThrow(ex).when(chain).doFilter(any(), any());
        response.setCommitted(true);

        assertThrows(IllegalStateException.class, () -> filter.doFilter(request, response, chain));
        verify(resolver, never()).resolveException(any(), any(), any(), any());
    }
}
