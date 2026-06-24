package br.gov.pr.idr.domain.shared.validation;

import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotificationValidation — Handler de validação por acúmulo")
class NotificationValidationTest {

    @Test
    @DisplayName("deve criar handler sem erros iniciais")
    void shouldCreateEmpty() {
        final var handler = NotificationValidation.create();
        assertFalse(handler.hasError());
        assertTrue(handler.getErrors().isEmpty());
    }

    @Nested
    @DisplayName("Acúmulo de erros")
    class Accumulate {

        @Test
        @DisplayName("deve acumular um erro")
        void shouldAccumulateOneError() {
            final var handler = NotificationValidation.create();
            handler.append(DomainError.from("campo", "mensagem de erro"));

            assertTrue(handler.hasError());
            assertEquals(1, handler.getErrors().size());
            assertEquals("campo", handler.getErrors().getFirst().field());
            assertEquals("mensagem de erro", handler.getErrors().getFirst().message());
        }

        @Test
        @DisplayName("deve acumular múltiplos erros")
        void shouldAccumulateMultipleErrors() {
            final var handler = NotificationValidation.create();
            handler.append(DomainError.from("a", "erro 1"));
            handler.append(DomainError.from("b", "erro 2"));
            handler.append(DomainError.from("c", "erro 3"));

            assertEquals(3, handler.getErrors().size());
        }
    }

    @Nested
    @DisplayName("Bloco validate()")
    class ValidateBlock {

        @Test
        @DisplayName("deve retornar resultado quando não lança exceção")
        void shouldReturnResultWhenNoException() {
            final var handler = NotificationValidation.create();
            final var result = handler.validate(() -> 42);

            assertEquals(42, result);
            assertFalse(handler.hasError());
        }

        @Test
        @DisplayName("deve capturar DomainException com erros e acumulá-los")
        void shouldCaptureDomainException() {
            final var handler = NotificationValidation.create();
            handler.validate(() -> {
                final var inner = NotificationValidation.create();
                inner.append(DomainError.from("campo", "erro de domínio"));
                throw new NotificationException(inner);
            });

            assertTrue(handler.hasError());
            assertEquals("erro de domínio", handler.getErrors().getFirst().message());
        }

        @Test
        @DisplayName("deve capturar RuntimeException genérica e adicionar erro")
        void shouldCaptureRuntimeException() {
            final var handler = NotificationValidation.create();
            handler.validate(() -> {
                throw new IllegalArgumentException("erro inesperado");
            });

            assertTrue(handler.hasError());
            assertTrue(handler.getErrors().getFirst().message().contains("erro inesperado"));
        }

        @Test
        @DisplayName("deve retornar null quando a validação lança exceção")
        void shouldReturnNullOnException() {
            final var handler = NotificationValidation.create();
            final var result = handler.validate(() -> {
                throw new RuntimeException("falha");
            });

            assertNull(result);
        }
    }
}
