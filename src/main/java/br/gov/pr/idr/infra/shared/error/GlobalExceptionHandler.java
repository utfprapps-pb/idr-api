package br.gov.pr.idr.infra.shared.error;

import br.gov.pr.idr.domain.shared.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.exceptions.UnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
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
        return new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Erro de validação", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Corpo da requisição ausente ou inválido",
                List.of(FieldError.from(ERROR, "O corpo da requisição é obrigatório")));
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return buildDomainErrorResponse(ex);
    }

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotificationException(NotificationException ex) {
        return buildDomainErrorResponse(ex);
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainExceptions(DomainException ex) {
        return buildDomainErrorResponse(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), message,
                List.of(FieldError.from(ERROR, message)));
    }

    private ErrorResponse buildDomainErrorResponse(DomainException ex) {
        final List<FieldError> errors = ex.getErrors() == null ? List.of() :
                ex.getErrors().stream()
                        .map(error -> FieldError.from(error.field(), error.message()))
                        .toList();
        return new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getContext(), errors);
    }
}
