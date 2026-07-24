package br.gov.pr.idr.domain.iam.user.vo;

import br.gov.pr.idr.domain.iam.user.exceptions.CPFException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CPF — Value Object")
class CPFTest {

    @Nested
    @DisplayName("Criação válida")
    class Valid {

        @Test
        @DisplayName("deve aceitar CPF formatado com pontos e hífen")
        void shouldAcceptFormattedCpf() {
            final var cpf = CPF.from("529.982.247-25");
            assertNotNull(cpf);
            assertEquals("529.982.247-25", cpf.value());
        }

        @Test
        @DisplayName("deve aceitar CPF apenas com dígitos")
        void shouldAcceptDigitsOnlyCpf() {
            final var cpf = CPF.from("52998224725");
            assertNotNull(cpf);
        }

        @ParameterizedTest
        @DisplayName("deve aceitar outros CPFs válidos")
        @ValueSource(strings = {"111.444.777-35", "123.456.789-09", "52998224725"})
        void shouldAcceptOtherValidCpfs(final String value) {
            assertDoesNotThrow(() -> CPF.from(value));
        }

        @Test
        @DisplayName("deve aceitar CPF cujo segundo dígito verificador calculado é 10 (mapeado para 0)")
        void shouldAcceptCpfWhenSecondCheckDigitMapsFrom10To0() {
            assertDoesNotThrow(() -> CPF.from("000.000.018-30"));
        }
    }

    @Nested
    @DisplayName("Criação inválida")
    class Invalid {

        @Test
        @DisplayName("deve rejeitar CPF nulo")
        void shouldRejectNull() {
            final var ex = assertThrows(CPFException.class, () -> CPF.from(null));
            assertTrue(ex.getMessage().contains("nulo ou vazio"));
        }

        @Test
        @DisplayName("deve rejeitar CPF em branco")
        void shouldRejectBlank() {
            assertThrows(CPFException.class, () -> CPF.from("   "));
        }

        @Test
        @DisplayName("deve rejeitar CPF com menos de 11 dígitos")
        void shouldRejectShortCpf() {
            assertThrows(CPFException.class, () -> CPF.from("1234567890"));
        }

        @Test
        @DisplayName("deve rejeitar CPF com todos os dígitos iguais")
        void shouldRejectRepeatingDigits() {
            assertThrows(CPFException.class, () -> CPF.from("111.111.111-11"));
        }

        @ParameterizedTest
        @DisplayName("deve rejeitar CPFs com dígitos verificadores errados")
        @ValueSource(strings = {"123.456.789-00", "000.000.000-00", "999.999.999-99"})
        void shouldRejectInvalidCheckDigits(final String value) {
            assertThrows(CPFException.class, () -> CPF.from(value));
        }

        @Test
        @DisplayName("deve rejeitar CPF com primeiro dígito verificador errado")
        void shouldRejectWrongFirstCheckDigit() {
            assertThrows(CPFException.class, () -> CPF.from("529.982.247-95"));
        }
    }
}
