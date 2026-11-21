package br.gov.pr.idr.infra.shared.error;

import br.gov.pr.idr.domain.iam.refresh_token.exceptions.InvalidRefreshTokenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * Ponto único de mapeamento exceção → resposta de erro no formato Problem Details (RFC 9457). Os erros do próprio
 * Spring MVC vêm de {@link ResponseEntityExceptionHandler}; o {@code type} de cada categoria vem de
 * {@link ProblemType}.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    static final String ERRORS = "errors";
    static final String INTERNAL_ERROR_DETAIL = "Erro inesperado, tente novamente mais tarde";
    private static final String ABOUT_BLANK = "about:blank";

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status,
            final WebRequest request) {
        final var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> FieldError.from(error.getField(), error.getDefaultMessage()))
                .toList();
        final var problem = ProblemType.VALIDACAO.toProblemDetail(status, "Erro de validação");
        problem.setProperty(ERRORS, errors);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatusCode status,
            final WebRequest request) {
        final var problem = ProblemType.VALIDACAO.toProblemDetail(status, "Corpo da requisição ausente ou inválido");
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Os 400 gerados pelo próprio Spring (tipo de parâmetro errado, parâmetro ausente) não têm categoria própria e
     * saem como {@code about:blank}; aqui passam a ser {@code validacao}, mantendo o {@code detail} do
     * {@code messages.properties}.
     */
    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(
            final Exception ex, final @Nullable Object body, final HttpHeaders headers, final HttpStatusCode statusCode,
            final WebRequest request) {
        // Alguns handlers da classe base passam body nulo e deixam o super montá-lo; monta aqui para poder ajustar.
        final Object resolvedBody = body == null && ex instanceof ErrorResponse errorResponse
                ? errorResponse.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale())
                : body;
        if (statusCode.value() == HttpStatus.BAD_REQUEST.value() && resolvedBody instanceof ProblemDetail problem
                && isAboutBlank(problem)) {
            problem.setType(ProblemType.VALIDACAO.type());
            problem.setTitle(ProblemType.VALIDACAO.title());
        }
        if (statusCode.is5xxServerError()) {
            log.error("Erro interno do Spring MVC", ex);
            if (resolvedBody instanceof ProblemDetail problem) {
                // Mesmo contrato do handler genérico: categoria erro-interno, sem detalhes internos.
                problem.setType(ProblemType.ERRO_INTERNO.type());
                problem.setTitle(ProblemType.ERRO_INTERNO.title());
                problem.setDetail(INTERNAL_ERROR_DETAIL);
            }
        }
        return super.handleExceptionInternal(ex, resolvedBody, headers, statusCode, request);
    }

    private static boolean isAboutBlank(final ProblemDetail problem) {
        return problem.getType() == null || ABOUT_BLANK.equals(problem.getType().toString());
    }

    /** Campo de {@code sort} desconhecido numa busca: erro do cliente, sem expor a entidade consultada. */
    @ExceptionHandler(PropertyReferenceException.class)
    public ProblemDetail handlePropertyReference(final PropertyReferenceException ex) {
        return ProblemType.VALIDACAO.toProblemDetail(HttpStatus.BAD_REQUEST,
                "Campo de ordenação inválido: " + ex.getPropertyName());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(final BadCredentialsException ex) {
        return ProblemType.CREDENCIAIS_INVALIDAS.toProblemDetail(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
    }

    @ExceptionHandler(DisabledException.class)
    public ProblemDetail handleDisabled(final DisabledException ex) {
        return ProblemType.CADASTRO_INATIVO.toProblemDetail(HttpStatus.FORBIDDEN,
                "Seu cadastro está inativo, solicite a um administrador para ativá-lo");
    }

    /** Falha de infraestrutura durante o login (ex.: banco fora do ar): não é erro de credencial. */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ProblemDetail handleInternalAuthenticationService(final InternalAuthenticationServiceException ex) {
        return handleGeneric(ex);
    }

    /** Sem token, token inválido ou expirado, usuário do token removido. */
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(final AuthenticationException ex) {
        return ProblemType.NAO_AUTENTICADO.toProblemDetail(HttpStatus.UNAUTHORIZED, null);
    }

    /** Autenticado sem a autoridade exigida pela rota ({@code hasAuthority}). */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(final AccessDeniedException ex) {
        return ProblemType.ACESSO_NEGADO.toProblemDetail(HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(final NotFoundException ex) {
        return ProblemType.NAO_ENCONTRADO.toProblemDetail(HttpStatus.NOT_FOUND, ex.getContext());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ProblemDetail handleInvalidRefreshToken(final InvalidRefreshTokenException ex) {
        return ProblemType.REFRESH_TOKEN_INVALIDO.toProblemDetail(HttpStatus.BAD_REQUEST, ex.getContext());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ProblemDetail handleUnprocessableEntity(final UnprocessableEntityException ex) {
        return ProblemType.REGRA_DE_NEGOCIO.toProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getContext());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(final ForbiddenException ex) {
        return ProblemType.ACESSO_NEGADO.toProblemDetail(HttpStatus.FORBIDDEN, ex.getContext());
    }

    @ExceptionHandler({NotificationException.class, DomainException.class})
    public ProblemDetail handleDomain(final DomainException ex) {
        final var problem = ProblemType.VALIDACAO.toProblemDetail(HttpStatus.BAD_REQUEST, ex.getContext());
        final List<FieldError> errors = ex.getErrors() == null ? List.of() : ex.getErrors().stream()
                .map(error -> FieldError.from(error.field(), error.message()))
                .toList();
        if (!errors.isEmpty()) {
            problem.setProperty(ERRORS, errors);
        }
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(final Exception ex) {
        log.error("Erro não tratado", ex);
        return ProblemType.ERRO_INTERNO.toProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_DETAIL);
    }
}
