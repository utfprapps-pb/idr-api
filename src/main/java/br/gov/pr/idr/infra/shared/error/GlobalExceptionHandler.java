package br.gov.pr.idr.infra.shared.error;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    static final String ERROR = "error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.add(FieldError.from(error.getField(), error.getDefaultMessage()))
        );
        return new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Erro de validação", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Corpo da requisição ausente ou inválido",
                List.of(FieldError.from(ERROR, "O corpo da requisição é obrigatório")));
    }

    /** Campo de {@code sort} desconhecido numa busca: erro do cliente, sem expor a entidade consultada. */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePropertyReference(PropertyReferenceException ex) {
        return new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Campo de ordenação inválido",
                List.of(FieldError.from("sort", "Campo de ordenação inválido: " + ex.getPropertyName())));
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return buildDomainErrorResponse(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotificationException(NotificationException ex) {
        return buildDomainErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException ex) {
        return buildDomainErrorResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainExceptions(DomainException ex) {
        return buildDomainErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), message,
                List.of(FieldError.from(ERROR, message)));
    }

    private ErrorResponse buildDomainErrorResponse(DomainException ex, HttpStatus status) {
        final List<FieldError> errors = ex.getErrors() == null ? List.of() :
                ex.getErrors().stream()
                        .map(error -> FieldError.from(error.field(), error.message()))
                        .toList();
        return new ErrorResponse(Instant.now(), status.value(), ex.getContext(), errors);
    }
}
