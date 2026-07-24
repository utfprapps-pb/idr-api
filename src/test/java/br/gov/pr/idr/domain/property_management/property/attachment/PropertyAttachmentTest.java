package br.gov.pr.idr.domain.property_management.property.attachment;

import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PropertyAttachment — Entity")
class PropertyAttachmentTest {

    private static final String FILE_NAME = "laudo.pdf";
    private static final String CONTENT_TYPE = "application/pdf";
    private static final long SIZE_BYTES = 1024L;

    @Nested
    @DisplayName("Criação via factory create()")
    class Create {

        @Test
        @DisplayName("deve criar anexo com dados válidos")
        void shouldCreateValidAttachment() {
            final var propertyId = PropertyID.unique();

            final var attachment = PropertyAttachment.create(propertyId, FILE_NAME, CONTENT_TYPE, SIZE_BYTES);

            assertNotNull(attachment.getId());
            assertEquals(FILE_NAME, attachment.getFileName());
            assertEquals(CONTENT_TYPE, attachment.getContentType());
            assertEquals(SIZE_BYTES, attachment.getSizeBytes());
            assertNotNull(attachment.getStorageKey());
            assertTrue(attachment.getStorageKey().contains(FILE_NAME));
        }

        @Test
        @DisplayName("deve rejeitar anexo com nome de arquivo nulo")
        void shouldRejectNullFileName() {
            final var propertyId = PropertyID.unique();

            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyAttachment.create(propertyId, null, CONTENT_TYPE, SIZE_BYTES));

            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome do arquivo")));
        }

        @Test
        @DisplayName("deve rejeitar anexo com nome de arquivo vazio")
        void shouldRejectBlankFileName() {
            final var propertyId = PropertyID.unique();

            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyAttachment.create(propertyId, "   ", CONTENT_TYPE, SIZE_BYTES));

            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Nome do arquivo")));
        }

        @Test
        @DisplayName("deve rejeitar anexo com tipo de conteúdo nulo")
        void shouldRejectNullContentType() {
            final var propertyId = PropertyID.unique();

            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyAttachment.create(propertyId, FILE_NAME, null, SIZE_BYTES));

            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Tipo de conteúdo")));
        }

        @Test
        @DisplayName("deve rejeitar anexo com tipo de conteúdo vazio")
        void shouldRejectBlankContentType() {
            final var propertyId = PropertyID.unique();

            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyAttachment.create(propertyId, FILE_NAME, "   ", SIZE_BYTES));

            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Tipo de conteúdo")));
        }

        @Test
        @DisplayName("deve rejeitar anexo com tamanho zero")
        void shouldRejectZeroSize() {
            final var propertyId = PropertyID.unique();

            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyAttachment.create(propertyId, FILE_NAME, CONTENT_TYPE, 0L));

            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Tamanho do anexo")));
        }

        @Test
        @DisplayName("deve rejeitar anexo com tamanho negativo")
        void shouldRejectNegativeSize() {
            final var propertyId = PropertyID.unique();

            final var ex = assertThrows(NotificationException.class,
                    () -> PropertyAttachment.create(propertyId, FILE_NAME, CONTENT_TYPE, -1L));

            assertTrue(ex.getErrors().stream().anyMatch(e -> e.message().contains("Tamanho do anexo")));
        }
    }

    @Nested
    @DisplayName("Reconstituição via with()")
    class With {

        @Test
        @DisplayName("deve reconstituir anexo a partir de dados persistidos")
        void shouldReconstitute() {
            final var id = PropertyAttachmentID.unique();

            final var attachment = PropertyAttachment.with(id, FILE_NAME, CONTENT_TYPE, SIZE_BYTES, "storage/key");

            assertEquals(id, attachment.getId());
            assertEquals(FILE_NAME, attachment.getFileName());
            assertEquals("storage/key", attachment.getStorageKey());
        }

        @Test
        @DisplayName("não deve validar dados ao reconstituir, mesmo que a chave de armazenamento seja nula")
        void shouldNotValidateOnReconstitution() {
            final var id = PropertyAttachmentID.unique();

            final var attachment = PropertyAttachment.with(id, FILE_NAME, CONTENT_TYPE, SIZE_BYTES, null);

            assertNull(attachment.getStorageKey());
        }
    }

    @Nested
    @DisplayName("Validação")
    class Validation {

        @Test
        @DisplayName("deve reportar erro de validação quando a chave de armazenamento é nula")
        void shouldReportErrorWhenStorageKeyIsNull() {
            final var attachment = PropertyAttachment.with(PropertyAttachmentID.unique(), FILE_NAME, CONTENT_TYPE, SIZE_BYTES, null);
            final var handler = NotificationValidation.create();

            attachment.validate(handler);

            assertTrue(handler.hasError());
            assertTrue(handler.getErrors().stream().anyMatch(e -> e.message().contains("Chave de armazenamento")));
        }

        @Test
        @DisplayName("deve reportar erro de validação quando a chave de armazenamento é vazia")
        void shouldReportErrorWhenStorageKeyIsBlank() {
            final var attachment = PropertyAttachment.with(PropertyAttachmentID.unique(), FILE_NAME, CONTENT_TYPE, SIZE_BYTES, "   ");
            final var handler = NotificationValidation.create();

            attachment.validate(handler);

            assertTrue(handler.hasError());
            assertTrue(handler.getErrors().stream().anyMatch(e -> e.message().contains("Chave de armazenamento")));
        }

        @Test
        @DisplayName("não deve reportar erros quando todos os dados são válidos")
        void shouldNotReportErrorsWhenValid() {
            final var attachment = PropertyAttachment.with(PropertyAttachmentID.unique(), FILE_NAME, CONTENT_TYPE, SIZE_BYTES, "storage/key");
            final var handler = NotificationValidation.create();

            attachment.validate(handler);

            assertFalse(handler.hasError());
        }
    }
}
