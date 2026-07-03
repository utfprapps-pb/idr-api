package br.gov.pr.idr.domain.property_management.property.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetPropertyQueryResult — parsing")
class GetPropertyQueryResultTest {

    private static final UUID PROPERTY_ID = UUID.randomUUID();
    private static final UUID PRODUCER_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();

    // Mesmos separadores usados pelo LISTAGG em PropertyJPARepository (Record/Unit Separator).
    private static final String RS = String.valueOf((char) 0x1E);
    private static final String FS = String.valueOf((char) 0x1F);

    private GetPropertyQueryResult build(String techniciansStr, String collaboratorsStr) {
        return build(techniciansStr, collaboratorsStr, null);
    }

    private GetPropertyQueryResult build(String techniciansStr, String collaboratorsStr, String attachmentsStr) {
        return new GetPropertyQueryResult(
                PROPERTY_ID, "Fazenda",
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                null, null,
                null, null,
                PRODUCER_ID, "Produtor",
                CITY_ID, "Curitiba",
                techniciansStr, collaboratorsStr, attachmentsStr
        );
    }

    @Test
    @DisplayName("deve retornar lista vazia quando techniciansStr é nulo")
    void shouldReturnEmptyTechniciansWhenNull() {
        final var result = build(null, null);

        assertTrue(result.technicians().isEmpty());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando techniciansStr está em branco")
    void shouldReturnEmptyTechniciansWhenBlank() {
        final var result = build("   ", null);

        assertTrue(result.technicians().isEmpty());
    }

    @Test
    @DisplayName("deve parsear técnicos a partir da string formatada")
    void shouldParseTechnicians() {
        final var techId = UUID.randomUUID();
        final var techStr = techId + FS + "João Técnico";

        final var result = build(techStr, null);

        assertEquals(1, result.technicians().size());
        assertEquals(techId, result.technicians().getFirst().id());
        assertEquals("João Técnico", result.technicians().getFirst().name());
    }

    @Test
    @DisplayName("deve parsear múltiplos técnicos separados pelo record separator")
    void shouldParseMultipleTechnicians() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var techStr = id1 + FS + "Ana" + RS + id2 + FS + "Carlos";

        final var result = build(techStr, null);

        assertEquals(2, result.technicians().size());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando collaboratorsStr é nulo")
    void shouldReturnEmptyCollaboratorsWhenNull() {
        final var result = build(null, null);

        assertTrue(result.collaborators().isEmpty());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando collaboratorsStr está em branco")
    void shouldReturnEmptyCollaboratorsWhenBlank() {
        final var result = build(null, "   ");

        assertTrue(result.collaborators().isEmpty());
    }

    @Test
    @DisplayName("deve parsear colaboradores a partir da string formatada")
    void shouldParseCollaborators() {
        final var collabId = UUID.randomUUID();
        final var collabStr = collabId + FS + "Maria Colaboradora" + FS + "8h";

        final var result = build(null, collabStr);

        assertEquals(1, result.collaborators().size());
        assertEquals(collabId, result.collaborators().getFirst().id());
        assertEquals("Maria Colaboradora", result.collaborators().getFirst().name());
        assertEquals("8h", result.collaborators().getFirst().hoursPerDay());
    }

    @Test
    @DisplayName("deve parsear múltiplos colaboradores separados pelo record separator")
    void shouldParseMultipleCollaborators() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var collabStr = id1 + FS + "Pedro" + FS + "6h" + RS + id2 + FS + "Clara" + FS + "8h";

        final var result = build(null, collabStr);

        assertEquals(2, result.collaborators().size());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando attachmentsStr é nulo")
    void shouldReturnEmptyAttachmentsWhenNull() {
        final var result = build(null, null, null);

        assertTrue(result.attachments().isEmpty());
    }

    @Test
    @DisplayName("deve retornar lista vazia quando attachmentsStr está em branco")
    void shouldReturnEmptyAttachmentsWhenBlank() {
        final var result = build(null, null, "   ");

        assertTrue(result.attachments().isEmpty());
    }

    @Test
    @DisplayName("deve parsear anexo a partir da string formatada")
    void shouldParseAttachments() {
        final var attachmentId = UUID.randomUUID();
        final var attachmentStr = attachmentId + FS + "contrato.pdf" + FS + "application/pdf" + FS + "2048";

        final var result = build(null, null, attachmentStr);

        assertEquals(1, result.attachments().size());
        assertEquals(attachmentId, result.attachments().getFirst().id());
        assertEquals("contrato.pdf", result.attachments().getFirst().fileName());
        assertEquals("application/pdf", result.attachments().getFirst().contentType());
        assertEquals(2048L, result.attachments().getFirst().sizeBytes());
    }

    @Test
    @DisplayName("deve parsear múltiplos anexos separados pelo record separator")
    void shouldParseMultipleAttachments() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var attachmentsStr = id1 + FS + "foto.png" + FS + "image/png" + FS + "1024"
                + RS + id2 + FS + "laudo.pdf" + FS + "application/pdf" + FS + "4096";

        final var result = build(null, null, attachmentsStr);

        assertEquals(2, result.attachments().size());
    }

    @Test
    @DisplayName("deve parsear anexo cujo nome de arquivo contém vírgula e pipe sem quebrar (regressão)")
    void shouldParseAttachmentWithCommaAndPipeInFileName() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var attachmentsStr = id1 + FS + "Planilha, versão | final.xlsx" + FS
                + "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" + FS + "4096"
                + RS + id2 + FS + "laudo.pdf" + FS + "application/pdf" + FS + "2048";

        final var result = build(null, null, attachmentsStr);

        assertEquals(2, result.attachments().size());
        assertEquals("Planilha, versão | final.xlsx", result.attachments().getFirst().fileName());
        assertEquals(4096L, result.attachments().getFirst().sizeBytes());
        assertEquals("laudo.pdf", result.attachments().get(1).fileName());
    }
}
