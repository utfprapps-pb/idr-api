package br.gov.pr.idr.domain.property_management.producer;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Producer — Aggregate")
class ProducerTest {

    private static final CPF VALID_CPF = CPF.from("529.982.247-25");

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar produtor com dados válidos")
        void shouldCreateValidProducer() {
            final var producer = Producer.create("João da Silva", VALID_CPF);

            assertNotNull(producer.getId());
            assertEquals("João da Silva", producer.getName());
            assertEquals(VALID_CPF, producer.getCpf());
        }

        @Test
        @DisplayName("deve rejeitar produtor sem nome")
        void shouldRejectNullName() {
            final var ex = assertThrows(NotificationException.class,
                    () -> Producer.create(null, VALID_CPF));
            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome do produtor")));
        }

        @Test
        @DisplayName("deve rejeitar produtor com nome em branco")
        void shouldRejectBlankName() {
            assertThrows(NotificationException.class, () -> Producer.create("   ", VALID_CPF));
        }

        @Test
        @DisplayName("deve criar produtor sem CPF (opcional)")
        void shouldCreateWithoutCpf() {
            assertDoesNotThrow(() -> Producer.create("João da Silva", null));
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir produtor a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = ProducerID.unique();
            final var producer = Producer.with(id, "Maria Souza", VALID_CPF);

            assertEquals(id, producer.getId());
            assertEquals("Maria Souza", producer.getName());
        }
    }

    @Nested
    @DisplayName("Atualização")
    class Update {

        @Test
        @DisplayName("deve atualizar nome e CPF")
        void shouldUpdateNameAndCpf() {
            final var producer = Producer.create("João", VALID_CPF);
            final var newCpf = CPF.from("123.456.789-09");

            producer.update("João Atualizado", newCpf);

            assertEquals("João Atualizado", producer.getName());
            assertEquals(newCpf, producer.getCpf());
        }

        @Test
        @DisplayName("deve rejeitar atualização com nome nulo")
        void shouldRejectUpdateWithNullName() {
            final var producer = Producer.create("João", VALID_CPF);
            assertThrows(NotificationException.class, () -> producer.update(null, VALID_CPF));
        }
    }
}
