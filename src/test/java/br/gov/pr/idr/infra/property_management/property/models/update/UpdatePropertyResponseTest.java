package br.gov.pr.idr.infra.property_management.property.models.update;

import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyOutput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UpdatePropertyResponse")
class UpdatePropertyResponseTest {

    @Test
    @DisplayName("deve mapear output sem anexos para uma lista vazia")
    void shouldMapOutputWithoutAttachments() {
        final var output = new UpdatePropertyOutput(UUID.randomUUID(), "Fazenda", List.of());

        final var response = UpdatePropertyResponse.from(output);

        assertEquals(output.id(), response.id());
        assertEquals(output.name(), response.name());
        assertTrue(response.attachments().isEmpty());
    }

    @Test
    @DisplayName("deve mapear anexos do output para AttachmentResponse")
    void shouldMapOutputAttachments() {
        final var attachmentId = UUID.randomUUID();
        final var summary = new UpdatePropertyOutput.AttachmentSummary(
                attachmentId, "documento.pdf", "application/pdf", 1024L);
        final var output = new UpdatePropertyOutput(UUID.randomUUID(), "Fazenda", List.of(summary));

        final var response = UpdatePropertyResponse.from(output);

        assertEquals(1, response.attachments().size());
        final var attachmentResponse = response.attachments().getFirst();
        assertEquals(attachmentId, attachmentResponse.id());
        assertEquals("documento.pdf", attachmentResponse.fileName());
        assertEquals("application/pdf", attachmentResponse.contentType());
        assertEquals(1024L, attachmentResponse.sizeBytes());
    }
}
