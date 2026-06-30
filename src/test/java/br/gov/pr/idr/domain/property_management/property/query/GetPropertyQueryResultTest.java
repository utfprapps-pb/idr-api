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

    private GetPropertyQueryResult build(String techniciansStr, String collaboratorsStr) {
        return new GetPropertyQueryResult(
                PROPERTY_ID, "Fazenda",
                BigDecimal.ZERO, BigDecimal.ZERO,
                0.0, 0.0, 0.0, 0.0,
                null, null,
                null, null,
                PRODUCER_ID, "Produtor",
                CITY_ID, "Curitiba",
                techniciansStr, collaboratorsStr
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
        final var techStr = techId + "|João Técnico";

        final var result = build(techStr, null);

        assertEquals(1, result.technicians().size());
        assertEquals(techId, result.technicians().getFirst().id());
        assertEquals("João Técnico", result.technicians().getFirst().name());
    }

    @Test
    @DisplayName("deve parsear múltiplos técnicos separados por vírgula")
    void shouldParseMultipleTechnicians() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var techStr = id1 + "|Ana," + id2 + "|Carlos";

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
        final var collabStr = collabId + "|Maria Colaboradora|8h";

        final var result = build(null, collabStr);

        assertEquals(1, result.collaborators().size());
        assertEquals(collabId, result.collaborators().getFirst().id());
        assertEquals("Maria Colaboradora", result.collaborators().getFirst().name());
        assertEquals("8h", result.collaborators().getFirst().hoursPerDay());
    }

    @Test
    @DisplayName("deve parsear múltiplos colaboradores separados por vírgula")
    void shouldParseMultipleCollaborators() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var collabStr = id1 + "|Pedro|6h," + id2 + "|Clara|8h";

        final var result = build(null, collabStr);

        assertEquals(2, result.collaborators().size());
    }
}
