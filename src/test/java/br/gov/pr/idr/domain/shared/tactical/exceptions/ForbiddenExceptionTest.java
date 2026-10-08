package br.gov.pr.idr.domain.shared.tactical.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ForbiddenException")
class ForbiddenExceptionTest {

    @Test
    @DisplayName("deve ser uma DomainException e carregar a mensagem informada")
    void shouldBeADomainExceptionAndCarryTheMessage() {
        final var ex = ForbiddenException.with("Usuário não tem acesso à propriedade");

        assertInstanceOf(DomainException.class, ex);
        assertEquals("Usuário não tem acesso à propriedade", ex.getContext());
        assertTrue(ex.getErrors().isEmpty());
    }
}
