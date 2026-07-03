package br.gov.pr.idr.infra.shared.error;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("deve retornar 400 para NotificationException com erros")
    void shouldHandle400ForNotificationException() {
        final var validation = NotificationValidation.create();
        validation.append(DomainError.from("name", "Nome é obrigatório"));
        final var ex = new NotificationException(validation);

        final var response = handler.handleNotificationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals(1, response.errors().size());
        assertEquals("Nome é obrigatório", response.errors().getFirst().message());
    }

    @Test
    @DisplayName("deve retornar 400 para DomainException")
    void shouldHandle400ForDomainException() {
        final var ex = DomainException.from("CPF já cadastrado");

        final var response = handler.handleDomainExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("CPF já cadastrado", response.message());
    }

    @Test
    @DisplayName("deve retornar 400 para HttpMessageNotReadableException")
    void shouldHandle400ForHttpMessageNotReadable() {
        final var ex = mock(HttpMessageNotReadableException.class);

        final var response = handler.handleHttpMessageNotReadable(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("Corpo da requisição ausente ou inválido", response.message());
        assertFalse(response.errors().isEmpty());
    }

    @Test
    @DisplayName("deve retornar 500 para Exception genérica com causa")
    void shouldHandle500ForGenericExceptionWithCause() {
        final var cause = new RuntimeException("erro interno");
        final var ex = new RuntimeException("wrapper", cause);

        final var response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status());
        assertEquals("erro interno", response.message());
    }

    @Test
    @DisplayName("deve retornar 500 para Exception genérica sem causa")
    void shouldHandle500ForGenericExceptionWithoutCause() {
        final var ex = new RuntimeException("mensagem direta");

        final var response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status());
        assertEquals("mensagem direta", response.message());
    }

    @Test
    @DisplayName("deve retornar 400 para MethodArgumentNotValidException com field errors")
    void shouldHandle400ForMethodArgumentNotValidException() {
        final var fieldError = mock(org.springframework.validation.FieldError.class);
        when(fieldError.getField()).thenReturn("nome");
        when(fieldError.getDefaultMessage()).thenReturn("Nome é obrigatório");

        final var bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        final var ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        final var response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("Erro de validação", response.message());
        assertEquals(1, response.errors().size());
        assertEquals("Nome é obrigatório", response.errors().getFirst().message());
    }
}
